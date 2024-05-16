package entity;

import java.awt.Rectangle;
import java.math.BigDecimal;

import item.ItemWeapon;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;
import weapon.Weapon;

public class ZombieKnight extends Enemy {
	private final Rectangle regularHitbox = CollisionChecker.getHitbox(4, 1, 12, 16);
	private final Rectangle attackHitbox = CollisionChecker.getHitbox(0, 1, 16, 16);
	Entity target;
	public ZombieKnight(Level level) {
		super(level);
		this.hitbox = regularHitbox;
		this.attackDamage = 3D;
		this.attackCooldown = 0;
		this.maxHealth = BigDecimal.valueOf(12.0D);
		this.health = maxHealth;
		// TODO Auto-generated constructor stub
	}
	public String getSprite() {
		return "enemy/zombieknight.png";
	}
	public void update() {
		super.update();
		if(this.attackCooldown == 0) {
			this.isAttacking = false;
			if(CollisionChecker.distance(this, level.player) < 8) {
				this.attackCooldown = 45;
				if(this.canGoTo((int)Math.round(level.player.x), (int)Math.round(level.player.y))) {
					this.target = level.player;
				}else {
					this.attackCooldown = 0;
				}
			}
		}else {
			if(!isAttacking && CollisionChecker.distanceTaxicab(this, target) > 1) {
				if(this.x > target.x) {
					this.goLeft(true);
				}else {
					this.goRight(true);
				}
			}else {
				this.attackCooldown--;
				if(attackCooldown < 0)
					attackCooldown = 0;
				this.isAttacking = true;
			}
		}
		if(isAttacking)
			if(CollisionChecker.checkHitboxes(this.attackHitbox, level.player.hitbox, x, y, level.player.x, level.player.y))
				level.player.damage(this.attackDamage);
	}
	public void render(WorldRenderer wr) {
		if(this.facing==Direction.LEFT) {
			wr.drawTiledImage(this.sprite, this.x - 0.5, this.y, 1.5, 1, 24, this.isAttacking?16:0, 0, this.isAttacking?32:16);
		} else {
			wr.drawTiledImage(this.sprite, this.x, this.y, 1.5, 1, 0, this.isAttacking?16:0, 24, this.isAttacking?32:16);
		}
	}
	public void onDied() {
		if((int)(Math.random() * 20) == 1) {
			Entity e = new DroppedItem(level, new ItemWeapon(Weapon.sword.id));
			e.setPosition(this.x, this.y);
			level.addEntity(e);
		}
	}

}
