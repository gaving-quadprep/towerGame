package entity;

import main.Main;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;

public class FloatingPlatform extends PlatformEntity {
	public double baseY;
	public double motion = 1.0D;
	public FloatingPlatform(Level level) {
		super(level);
		canBeStoodOn = true;
		hitbox = CollisionChecker.getHitbox(0, 6, 16, 10);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update() {
		yVelocity = baseY+motion*Math.sin((Main.frames)/30.0D) - y;
		y += yVelocity;
	}
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		baseY = y;
	}
	@Override
	public String getSprite() {
		return "platform.png";
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(motion, "motion");
		sd.setObject(baseY, "baseY");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		motion = (double)sd.getObjectDefault("isBlue", 1.0D); // What the sigma
		baseY = (double)sd.getObjectDefault("baseY", y);
	}

}
