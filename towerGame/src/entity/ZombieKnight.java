package entity;

import java.awt.Rectangle;
import java.math.BigDecimal;

import main.WorldRenderer;
import map.Level;
import map.Tile;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class ZombieKnight extends Enemy {
	private final Rectangle regularHitbox = CollisionChecker.getHitbox(4, 0, 12, 16);
	private final Rectangle attackHitbox = CollisionChecker.getHitbox(0, 0, 16, 16);
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
			if(CollisionChecker.distanceTaxicab(this, target) > 1) {
				if(this.x > target.x) {
					this.facing=Direction.LEFT;
					
					CollisionChecker.checkForTileTouch(this.level, this, Direction.LEFT, 0.051);
					if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.051)) {
						this.x -= 0.051;
					}else {
						if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.051/4)) {
							this.x -= 0.051/4;
						}else {
							this.y -= 0.5625;
							if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.051) && onGround) {
								this.x -= 0.051;
								this.y += 0.46;
							}else {
								this.y += 0.5625;
							}
						}
					}
					this.xVelocity -= 0.00051;
				}else {
					this.facing=Direction.RIGHT;
					CollisionChecker.checkForTileTouch(this.level, this, Direction.RIGHT, 0.051);
					if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.051)) {
						this.x += 0.051;
					}else {
						if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.051/4)) {
							this.x += 0.051/4;
						}else {
							this.y -= 0.5625;
							if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.051) && onGround) {
								this.x += 0.051;
								this.y += 0.46;
							}else {
								this.y += 0.5625;
							}
						}
					}
					this.xVelocity += 0.00051;
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

}
