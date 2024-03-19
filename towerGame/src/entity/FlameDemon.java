package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.math.BigDecimal;

import main.Main;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class FlameDemon extends Enemy {
	BufferedImage attackSprite;
	private boolean onGroundPrev = false;
	double attackSpread = 0;
	private static final Rectangle attackHitbox = new Rectangle(0, 30, 2, 32);
	
	public FlameDemon(Level level) {
		super(level);
		this.hitbox = new Rectangle(0, 0, 32, 32);
		this.attackDamage = 7.5D;
		this.attackCooldown = 300;
		this.maxHealth = BigDecimal.valueOf(25.0D);
		this.health = maxHealth;
		this.attackSprite = level.getSprite("flamedemonattack.png");
		// TODO Auto-generated constructor stub
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if((positions[0]+(32*Main.scale) > 0 && positions[0] < 320*Main.scale)&&(positions[1]+(32*Main.scale) > 0 && positions[1] < 240*Main.scale)) {
			if(this.facing==Direction.LEFT) {
				g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize*2, positions[1]+Main.tileSize*2, this.isAttacking?32:16, 0, this.isAttacking?16:0, 16, (ImageObserver)null);
			} else {
				g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize*2, positions[1]+Main.tileSize*2, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16, (ImageObserver)null);
			}
			if(this.attackSpread > 0) {
				g2.drawImage(this.attackSprite, positions[0]-(int)(this.attackSpread*Main.tileSize)+Main.tileSize, positions[1]+29*Main.scale, 6*Main.scale, 3*Main.scale, (ImageObserver)null);
				g2.drawImage(this.attackSprite, positions[0]+(int)(this.attackSpread*Main.tileSize)+Main.tileSize, positions[1]+29*Main.scale, -6*Main.scale, 3*Main.scale, (ImageObserver)null);
			}
		}
	}
	public String getSprite() {
		return "enemy/flamedemon.png";
	}
	public void update() {
		onGroundPrev = this.onGround;
		super.update();
		if(CollisionChecker.checkHitboxes(level.player.hitbox, attackHitbox, level.player.x, level.player.y, this.x+this.attackSpread, this.y)) {
			level.player.damage(2);
			level.player.damageTimer = 20;
		}
		if(CollisionChecker.checkHitboxes(level.player.hitbox, attackHitbox, level.player.x, level.player.y, this.x-this.attackSpread, this.y)) {
			level.player.damage(2);
			level.player.damageTimer = 20;
		}
		if(this.xVelocity >= 0) {
			this.facing = Direction.RIGHT;
		}else {
			this.facing = Direction.LEFT;
		}
		if(this.attackCooldown == 0 && this.onGround && Math.abs(this.x-level.player.x) < 14 ) {
			this.attackCooldown = 210 + (int)(Math.random() * 21);
			this.isAttacking = true;
			double angle=(double)Math.atan2((this.level.player.x)-this.x, this.level.player.y-this.y);
			this.xVelocity= (double)Math.sin(angle) / 13;
			this.yVelocity = -0.17;
			this.onGround = false;
		}
		if(this.onGround && !this.onGroundPrev) {
			if(this.isAttacking) {
				this.isAttacking = false;
				this.attackSpread = 1;
			}
			this.yVelocity = 0;
		}
		attackCooldown--;
		if( attackCooldown < 0) {
			attackCooldown = 0;
		}
		if(this.attackSpread > 0) {
			this.attackSpread += 0.1;
		}
		if(this.attackSpread > 4) {
			this.attackSpread = 0;
		}
	}
	public int getSpriteWidth() {
		return 32;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.attackSpread, "attackSpread");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.attackSpread = (double)sd.getObjectDefault("attackSpread", 0d);
	}

}
