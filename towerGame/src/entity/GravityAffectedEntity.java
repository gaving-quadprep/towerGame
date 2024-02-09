package entity;

import java.awt.Graphics2D;

import main.CollisionChecker;
import main.Direction;
import main.Main;
import map.Level;
import save.SerializedData;

public abstract class GravityAffectedEntity extends Entity {
	public double xVelocity;
	public double yVelocity;
	public boolean onGround=false;
	public GravityAffectedEntity(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(0,0,16,16);
	}
	public void update() {
		super.update();
		this.yVelocity+=0.007F;//gravity
		
		CollisionChecker.checkForTileTouch(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity);
		
		if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.y+=yVelocity;
			this.onGround=false;
		}else {
	
			if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/3)) {
				this.y+=yVelocity/3;
			}
			if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/7)) {
				this.y+=yVelocity/7;
			}
			if(this.yVelocity>0) {
				this.onGround=true;
				this.xVelocity /= 1.2;
			}else {
				this.onGround=false;
			}
			this.yVelocity=yVelocity>0?-(this.yVelocity/8):-(this.yVelocity); //bounce
		}
		if(this.xVelocity != 0.0F) {
			if(!CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
				this.x+=xVelocity;
			}else {
				this.xVelocity= -(this.xVelocity/11);
			}
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
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		this.yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		this.onGround = (boolean)sd.getObjectDefault("onGround", false);
	}
}