package entity;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import main.Main;
import map.Level;
import util.CollisionChecker;
import util.Direction;

public class Thing extends Enemy {
	public Thing(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(2, 0, 14, 16);
		this.attackCooldown = 60;
		this.health = 5;
		this.maxHealth = 5;
		this.attackDamage = 5;
	}
	public String getSprite() {
		return "thing.png";
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if(this.facing==Direction.LEFT) {
			g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize, positions[1]+Main.tileSize, this.isAttacking?32:16, 0, this.isAttacking?16:0, 16, (ImageObserver)null);
		} else {
			g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize, positions[1]+Main.tileSize, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16, (ImageObserver)null);
		}
	}
	public void update() {
		super.update();
		if(this.xVelocity >= 0) {
			this.facing = Direction.RIGHT;
		}else {
			this.facing = Direction.LEFT;
		}
		if(this.attackCooldown == 0 && this.onGround && Math.hypot(Math.abs(this.x-level.player.x), Math.abs(this.y-level.player.y)) < 6 ) {
			this.attackCooldown = 170 + (int)(Math.random() * 21);
			this.isAttacking = true;
			double angle=(double)Math.atan2((this.level.player.x)-this.x, this.level.player.y-this.y);
			this.xVelocity=(double) Math.sin(angle)/7.5;
			this.yVelocity=(double) (Math.cos(angle)/4.5)-0.1 - (0.002 * Math.abs(this.level.player.x-this.x));
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
