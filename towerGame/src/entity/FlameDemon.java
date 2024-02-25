package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import main.Direction;
import main.Main;
import map.Level;

public class FlameDemon extends Enemy {
	BufferedImage attackSprite;
	double attackSpreadOutward;
	
	public FlameDemon(Level level) {
		super(level);
		this.hitbox = new Rectangle(0, 0, 32, 32);
		this.attackDamage = 7.5D;
		this.attackCooldown = 360;
		this.maxHealth = 25.0D;
		this.health = maxHealth;
		this.attackSprite = level.getSprite("flamedemonattack.png");
		// TODO Auto-generated constructor stub
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if(this.facing==Direction.LEFT) {
			g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize*2, positions[1]+Main.tileSize*2, this.isAttacking?32:16, 0, this.isAttacking?16:0, 16, (ImageObserver)null);
		} else {
			g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize*2, positions[1]+Main.tileSize*2, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16, (ImageObserver)null);
		}
	}
	public String getSprite() {
		return "flamedemon.png";
	}
	public void update() {
		super.update();
		if(this.xVelocity >= 0) {
			this.facing = Direction.RIGHT;
		}else {
			this.facing = Direction.LEFT;
		}
		if(this.attackCooldown == 0 && this.onGround && Math.abs(this.x-level.player.x) < 14 ) {
			this.attackCooldown = 210 + (int)(Math.random() * 21);
			this.isAttacking = true;
			double angle=(double)Math.atan2((this.level.player.x)-this.x, this.level.player.y-this.y);
			this.xVelocity= (double)Math.sin(angle) / 11;
			this.yVelocity = -0.17;
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
