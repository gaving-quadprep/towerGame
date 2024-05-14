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
	public int targetX;
	public ZombieKnight(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(4, 0, 16, 16);
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
					this.targetX = (int)Math.round(level.player.x);
				}else {
					this.targetX = (int)Math.round(this.x);
				}
			}
		}else {
			if(CollisionChecker.distanceTaxicab(this, level.player) > 1) {
				if(this.x > level.player.x) {
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
				this.isAttacking = true;
			}
		}
	}
	public void render(WorldRenderer wr) {
		if(this.facing==Direction.LEFT) {
			wr.drawTiledImage(this.sprite, this.x - 0.5, this.y, 1.5, 1, 24, this.isAttacking?16:0, 0, this.isAttacking?32:16);
		} else {
			wr.drawTiledImage(this.sprite, this.x, this.y, 1.5, 1, 0, this.isAttacking?16:0, 24, this.isAttacking?32:16);
		}
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.targetX, "targetX");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.targetX = (int)sd.getObjectDefault("targetX", this.x);
	}

}
