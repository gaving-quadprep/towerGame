package entity;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import main.Main;
import map.Level;

public class PuddleMonster extends Enemy {
	private int timeLeftBeforeAttacking;
	public PuddleMonster(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}
	public void update() {
		super.update();
		this.attackDamage = this.isAttacking ? 1.5 : 0;
		this.shouldRenderHealthBar = this.isAttacking;
	}
	public String getSprite() {
		return "enemy/puddle.png";
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if((positions[0]+(16*Main.scale) > 0 && positions[0] < 320*Main.scale)&&(positions[1]+(16*Main.scale) > 0 && positions[1] < 240*Main.scale))
			g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize, positions[1]+Main.tileSize, this.isAttacking?32:0, 0, this.isAttacking?48:16, 16, (ImageObserver)null);
	}
	public void damage(double damage) {
		super.damage(damage);
	}
}
