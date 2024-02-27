package entity;

import java.awt.Graphics2D;

import main.Main;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public abstract class GravityAffectedEntity extends Entity {
	public double xVelocity;
	public double yVelocity;
	public double airResistance = 1.01;
	public boolean onGround=false;
	public GravityAffectedEntity(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(0,0,16,16);
	}
	public void update() {
		super.update();
		this.yVelocity+=0.007D;
		
		CollisionChecker.checkForTileTouch(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity);
		boolean touch = false;
		Entity touchedEntity = null;
		if(CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity))
			touch = true;
		if(yVelocity >= 0) {
			for(Entity e : level.entities) {
				if(e.canBeStoodOn) {
					if(CollisionChecker.checkEntities(this, e)) {
						double eTopY = e.y + (double)e.hitbox.y/16;
						double newY = eTopY - (double)this.hitbox.y/16 - (double)this.hitbox.height/16;
						if(Math.abs(y-newY)<0.125 && !CollisionChecker.checkTile(this.level, this, (y-newY)<0?Direction.DOWN:Direction.UP, Math.abs(y-newY))) {
							touch=true;
							touchedEntity = e;
							this.y = newY;
						}
					}
				}
			}
		}
		if(!touch) {
			this.y+=yVelocity;
			this.onGround=false;
		}else {
	
			if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/4)) {
				this.y+=yVelocity/4;
			}
			if(this.yVelocity>0) {
				this.onGround=true;
				this.xVelocity /= 1.2;
			}else {
				this.onGround=false;
			}
			if(touchedEntity != null) {
				this.yVelocity=0;
			}else {
				this.yVelocity=yVelocity>0?-(this.yVelocity/8):-(this.yVelocity); //bounce
			}
			
		}
		this.xVelocity /= airResistance;
		if(this.xVelocity != 0.0F) {
			if(!CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
				this.x+=xVelocity;
			}else {
				this.xVelocity= -(this.xVelocity/11);
			}
			CollisionChecker.checkForTileTouch(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity);
		}
	}
	public void move(double motion, Direction direction) {
		if(!CollisionChecker.checkTile(this.level, this, direction, motion)) {
			super.move(motion, direction);
		}
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite,positions[0],positions[1],Main.tileSize,Main.tileSize,null);
	}
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