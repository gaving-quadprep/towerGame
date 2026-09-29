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
		hitbox = CollisionChecker.getHitbox(1, 1, 15, 15);
	}
	@Override
	public void update() {
		super.update();
		yVelocity += level.gravity;

		touch = false;
		touchedEntity = null;
		if(CollisionChecker.checkTileAndTileTouch(level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity))
			touch = true;
		if(yVelocity >= 0) {
			level.forEachEntityOfType(PlatformEntity.class, true, (e) -> {
				if(e.canBeStoodOn && CollisionChecker.checkEntities(this, e)) {
					double eTopY = e.y + (double)e.hitbox.y/16;
					double newY = eTopY - (double)hitbox.y/16 - (double)hitbox.height/16;
					if((y-newY<0.2+yVelocity && y-newY> -0.1) && !CollisionChecker.checkTile(level, this, (y-newY)<0?Direction.DOWN:Direction.UP, Math.abs(y-newY)) && yVelocity >= 0) {
						touch = true;
						touchedEntity = e;
						y = newY;
					}
				}
			});
		}
		if(!touch) {
			y+=yVelocity;
			onGround=false;
		}else {

			if(!CollisionChecker.checkTileAndTileTouch(level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/4)) {
				y+=yVelocity/4;
			}
			if(yVelocity>0) {
				onGround=true;
				xVelocity /= 1.2;
			}else {
				onGround=false;
			}
			if(touchedEntity != null) {
				onHit(Direction.DOWN);
				yVelocity=0;
			}else {
				if(yVelocity>0) {
					onHit(Direction.DOWN);
				}else {
					onHit(Direction.UP);
				}
				yVelocity=yVelocity>0?0:-(yVelocity / 2); //don't bounce
			}

		}
		xVelocity /= airResistance;
		if(xVelocity != 0.0F) {
			if(!CollisionChecker.checkTileAndTileTouch(level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
				x+=xVelocity;
			}else {
				if(xVelocity>0) {
					onHit(Direction.RIGHT);
				}else {
					onHit(Direction.LEFT);
				}
				xVelocity= -(xVelocity/11);
			}
		}

		CollisionChecker.runWhileTileTouched(level, this);

		if(y > level.sizeY + 40) {
			markedForRemoval = true;
		}
	}

	@Override
	public void move(double motion, Direction direction) {
		if(!CollisionChecker.checkTile(level, this, direction, motion)) {
			super.move(motion, direction);
		}
	}

	public void onHit(Direction direction) {}

	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(xVelocity, "xVelocity");
		sd.setObject(yVelocity, "yVelocity");
		sd.setObject(onGround, "onGround");
		sd.setObject(airResistance, "airResistance");
		return sd;
	}

	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		onGround = (boolean)sd.getObjectDefault("onGround", false);
		airResistance = (double)sd.getObjectDefault("airResistance",1.01);
	}
}