package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import main.Main;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class FlameDemon extends Enemy {
	BufferedImage attackSprite;
	private boolean onGroundPrev = false;
	int attackSpread = 0;
	private static final Rectangle attackHitbox = new Rectangle(14, 30, 2, 2);
	
	public FlameDemon(Level level) {
		super(level);
		this.hitbox = new Rectangle(0, 0, 32, 32);
		this.attackDamage = 7.5D;
		this.attackCooldown = 150;
		this.maxHealth = BigDecimal.valueOf(25.0D);
		this.health = maxHealth;
		// TODO Auto-generated constructor stub
	}
	
	public void loadSprites() {
		super.loadSprites();
		this.attackSprite = level.getSprite("flamedemonattack.png");
	}
	public void render(WorldRenderer wr) {
		if(this.facing == Direction.LEFT) {
			wr.drawTiledImage(this.sprite, this.x, this.y, 2, 2, this.isAttacking?32:16, 0, this.isAttacking?16:0, 16);
		} else {
			wr.drawTiledImage(this.sprite, this.x, this.y, 2, 2, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16);
		}
		if(this.attackSpread > 0) {
			wr.drawImage(this.attackSprite, this.x-(((double)this.attackSpread/10)-1), this.y+(29D/16), (6D/16), (3D/16));
			wr.drawImage(this.attackSprite, this.x+(((double)this.attackSpread/10)+1), this.y+(29D/16), -(6D/16), (3D/16));
		}
	}
	public String getSprite() {
		return "enemy/flamedemon.png";
	}
	public void update() {
		onGroundPrev = this.onGround;
		super.update();
		if(CollisionChecker.checkHitboxes(level.player.hitbox, attackHitbox, level.player.x, level.player.y, this.x+(double)this.attackSpread/10, this.y)) {
			level.player.damage(2);
			level.player.damageTimer = 24;
		}
		if(CollisionChecker.checkHitboxes(level.player.hitbox, attackHitbox, level.player.x, level.player.y, this.x-(double)this.attackSpread/10, this.y)) {
			level.player.damage(2);
			level.player.damageTimer = 24;
		}
		if(this.xVelocity >= 0) {
			this.facing = Direction.RIGHT;
		} else {
			this.facing = Direction.LEFT;
		}
		if(this.attackCooldown == 0 && this.onGround && Math.abs(this.x-level.player.x) < 14 ) {
			this.attackCooldown = 160 + Main.random.nextInt(21);
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
		if(attackCooldown < 0) {
			attackCooldown = 0;
		}
		if(this.attackSpread > 0) {
			this.attackSpread++;
		}
		if(this.attackSpread > 40) {
			this.attackSpread = 0;
		}
	}
	public int getSpriteWidth() {
		return 32;
	}
	public String getDebugString() {
		return "attackSpread: " + this.attackSpread + "\nonGroundPrev: " + this.onGroundPrev;
	}
	//backwards compatability
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(((double)this.attackSpread) / 10, "attackSpread");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.attackSpread = (int)((double)sd.getObjectDefault("attackSpread", 0d) * 10);
	}

}
