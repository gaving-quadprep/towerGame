package entity;

import java.awt.Graphics2D;

import main.CollisionChecker;
import main.Main;
import map.Level;

public class FloatingPlatform extends GravityAffectedEntity {
	public double baseY;
	public FloatingPlatform(Level level) {
		super(level);
		this.canBeStoodOn = true;
		this.hitbox = CollisionChecker.getHitbox(0, 6, 16, 10);
		// TODO Auto-generated constructor stub
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite,positions[0],positions[1],Main.tileSize,Main.tileSize,null);
	}
	public void update() {
		this.yVelocity = baseY+(double) Math.sin(((double)Main.frames)/30.0D) - this.y;
		this.y += this.yVelocity;
	}
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		this.baseY=y;
	}
	public String getSprite() {
		return "platform.png";
	}

}
