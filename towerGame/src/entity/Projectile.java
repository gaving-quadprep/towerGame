package entity;

import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class Projectile extends GravityAffectedEntity {
	public boolean hasBeenReflected = false;
	public long createTime;
	public Projectile(Level level) {
		super(level);
		this.createTime = System.currentTimeMillis();
		// TODO Auto-generated constructor stub
	}
	public void update() {
		this.yVelocity+=0.007D;//gravity
		
		CollisionChecker.checkForTileTouch(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity);
		
		if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.y+=yVelocity;
		}else {
			this.markedForRemoval = true;
		}
		this.xVelocity /= 1.01;
		if(this.xVelocity != 0.0D) {
			if(!CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
				this.x+=xVelocity;
			}else {
				this.xVelocity= -(this.xVelocity/11);
			}
			CollisionChecker.checkForTileTouch(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity);
		}
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.createTime, "createTime");
		sd.setObject(this.hasBeenReflected, "hasBeenReflected");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.createTime = (long)sd.getObjectDefault("createTime",-1);
		this.hasBeenReflected = (boolean)sd.getObjectDefault("hasBeenReflected",false);
	}

}
