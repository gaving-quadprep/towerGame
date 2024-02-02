package entity;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import main.Main;
import map.Level;

public class Thing extends Enemy {
	private static final long serialVersionUID = 3439766573016432470L;
	public boolean isAttacking = false;
	public Thing(Level level) {
		super(level);
		this.attackCooldown = 60;
		this.health = 5;
		this.maxHealth = 5;
		this.attackDamage = 5.0F;
	}
	public String getSprite() {
		return "thing.png";
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize, positions[1]+Main.tileSize, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16, (ImageObserver)null);
	}
	public void update() {
		super.update();
		if(this.attackCooldown == 0 && Math.hypot(Math.abs(this.x-level.player.x), Math.abs(this.y-level.player.y)) < 6 ) {
			this.attackCooldown = 180;
			this.isAttacking = true;
			double angle=(double)Math.atan2((this.level.player.x)-this.x, this.level.player.y-this.y);
			this.xVelocity=(double) Math.sin(angle)/8F;
			this.yVelocity=(double) (Math.cos(angle)/4.5F)-0.1F - (0.002F * Math.abs(this.level.player.x-this.x));
			this.onGround = false;
		}
		if(this.onGround) {
			this.isAttacking = false;
		}
		attackCooldown--;
		if( attackCooldown < 0) {
			attackCooldown = 0;
		}
	}
}
