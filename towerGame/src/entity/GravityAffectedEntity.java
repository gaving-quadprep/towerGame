package entity;

import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public abstract class GravityAffectedEntity extends Entity {
	public double xVelocity;
	public double yVelocity;
	public double airResistance = 1.01;
	public boolean onGround=false;
	
	// TEMPORARY, USED TO FIX "LOCAL VARIABLE MUST BE FINAL"
	private boolean touch;
	private Entity touchedEntity;
	
	public GravityAffectedEntity(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(1, 1, 15, 15);
	}
	public void update() {
		super.update();
		this.yVelocity += level.gravity;
		
		touch = false;
		touchedEntity = null;
		if(CollisionChecker.checkTileAndTileTouch(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity))
			touch = true;
		if(yVelocity >= 0) {
			this.level.forEachEntityOfType(PlatformEntity.class, true, (e) -> {
				if(e.canBeStoodOn) {
					if(CollisionChecker.checkEntities(this, e)) {
						double eTopY = e.y + (double)e.hitbox.y/16;
						double newY = eTopY - (double)this.hitbox.y/16 - (double)this.hitbox.height/16;
						if((y-newY<0.2+this.yVelocity && y-newY> -0.1) && !CollisionChecker.checkTile(this.level, this, (y-newY)<0?Direction.DOWN:Direction.UP, Math.abs(y-newY)) && this.yVelocity >= 0) {
							touch = true;
							touchedEntity = e;
							this.y = newY;
						}
					}
				}
			});
		}
		if(!touch) {
			this.y+=yVelocity;
			this.onGround=false;
		}else {
	
			if(!CollisionChecker.checkTileAndTileTouch(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/4)) {
				this.y+=yVelocity/4;
			}
			if(this.yVelocity>0) {
				this.onGround=true;
				this.xVelocity /= 1.2;
			}else {
				this.onGround=false;
			}
			if(touchedEntity != null) {
				this.onHit(Direction.DOWN);
				this.yVelocity=0;
			}else {
				if(this.yVelocity>0) {
					this.onHit(Direction.DOWN);
				}else {
					this.onHit(Direction.UP);
				}
				this.yVelocity=yVelocity>0?0:-(this.yVelocity / 2); //don't bounce
			}
			
		}
		this.xVelocity /= airResistance;
		if(this.xVelocity != 0.0F) {
			if(!CollisionChecker.checkTileAndTileTouch(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
				this.x+=xVelocity;
			}else {
				if(this.xVelocity>0) {
					this.onHit(Direction.RIGHT);
				}else {
					this.onHit(Direction.LEFT);
				}
				this.xVelocity= -(this.xVelocity/11);
			}
		}

		CollisionChecker.runWhileTileTouched(level, this);
		
		if(this.y > level.sizeY + 40) {
			this.markedForRemoval = true;
		}
	}
	
	public void move(double motion, Direction direction) {
		if(!CollisionChecker.checkTile(this.level, this, direction, motion)) {
			super.move(motion, direction);
		}
	}
	
	public void onHit(Direction direction) {}
	
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.xVelocity, "xVelocity");
		sd.setObject(this.yVelocity, "yVelocity");
		sd.setObject(this.onGround, "onGround");
		sd.setObject(this.airResistance, "airResistance");
		return sd;
	}
	
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		this.yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		this.onGround = (boolean)sd.getObjectDefault("onGround", false);
		this.airResistance = (double)sd.getObjectDefault("airResistance",1.01);
	}
}