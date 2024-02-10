package entity;

import main.CollisionChecker;
import main.Direction;
import map.Level;

public class Projectile extends GravityAffectedEntity {

	public Projectile(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}
	public void update() {
		this.yVelocity+=0.007F;//gravity
		
		CollisionChecker.checkForTileTouch(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity);
		
		if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.y+=yVelocity;
		}else {
			this.markedForRemoval = true;
		}
		this.xVelocity /= 1.01;
		if(this.xVelocity != 0.0F) {
			if(!CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
				this.x+=xVelocity;
			}else {
				this.xVelocity= -(this.xVelocity/11);
			}
			CollisionChecker.checkForTileTouch(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity);
		}
	}

}
