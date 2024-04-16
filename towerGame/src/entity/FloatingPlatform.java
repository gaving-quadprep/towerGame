package entity;

import main.Main;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;

public class FloatingPlatform extends GravityAffectedEntity {
	public double baseY;
	public double motion= 1.0D;
	public FloatingPlatform(Level level) {
		super(level);
		this.canBeStoodOn = true;
		this.hitbox = CollisionChecker.getHitbox(0, 6, 16, 10);
		// TODO Auto-generated constructor stub
	}
	public void update() {
		this.yVelocity = baseY+(double) motion*Math.sin(((double)Main.frames)/30.0D) - this.y;
		this.y += this.yVelocity;
	}
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		this.baseY=y;
	}
	public String getSprite() {
		return "platform.png";
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.motion, "motion");
		sd.setObject(this.baseY, "baseY");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.motion = (double)sd.getObjectDefault("isBlue",1.0D);
		this.baseY = (double)sd.getObjectDefault("baseY",this.y);
	}

}
