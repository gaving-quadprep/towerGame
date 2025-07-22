package entity;

import java.math.BigDecimal;

import map.Level;
import map.Tile;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class LivingEntity extends GravityAffectedEntity {
	public BigDecimal health = BigDecimal.TEN;
	public BigDecimal maxHealth = BigDecimal.TEN;
	public Direction facing = Direction.RIGHT;
	public int damageTimer;
	public int damageCooldown = 10;
	public boolean shouldRenderHealthBar = true;
	public boolean invulnerable = false;
	public LivingEntity(Level level) {
		super(level);
	}
	public boolean canGoTo(int x, int y) {
		int y1 = (int) Math.round(this.y + this.hitbox.y/16 + this.hitbox.height/32);
		for(int x1 = (int) Math.round(this.x + this.hitbox.x/16 + this.hitbox.width/32); x1 != x; x1 += (x1 > x ? -1 : 1)) {
			if(!canStandOn(x1, y1)) {
				if(canStandOn(x1, y1+1) && !Tile.tiles[level.getTileForeground(x1, y1-1)].isSolid) {
					y1++;
				}else if(canStandOn(x1, y1-1) && !Tile.tiles[level.getTileForeground(x1 - (x1 > x ? -1 : 1), y1-1)].isSolid){
					y1--;
				}else {
					return false;
				}
			}
		}
		return --y1 <= y;
	}
	public boolean canStandOn(int x, int y) {
		return (!Tile.tiles[level.getTileForeground(x, y)].isSolid) && Tile.tiles[level.getTileForeground(x, y+1)].isSolid;
	}
	public boolean canJump() {
		if(this.onGround) {
			return true;
		} else {
			int[] positions = CollisionChecker.getTilePositions(level, this, Direction.DOWN, 0);
			if(this.level.getTileForeground(positions[0], positions[2]) == Tile.jumpTile.id) {
				return true;
			}
			if(this.level.getTileForeground(positions[1], positions[2]) == Tile.jumpTile.id) {
				return true;
			}
			if(this.level.getTileForeground(positions[0], positions[3]) == Tile.jumpTile.id) {
				return true;
			}
			if(this.level.getTileForeground(positions[1], positions[3]) == Tile.jumpTile.id) {
				return true;
			}
			return false;
		}
	}
	public void jump() {
		if(this.canJump()) {
			this.yVelocity =- 0.1582F;
			if(CollisionChecker.checkSpecificTile(this.level, this, Direction.DOWN, 0, Tile.jumpPad) || CollisionChecker.checkSpecificTile(this.level, this, Direction.UP, 0, Tile.jumpPad)) {
				this.yVelocity -= 0.0342F;
			}
		}
	}
	
	public void goLeft(boolean autoJump, double speed) {
		this.facing = Direction.LEFT;
		speed *= 0.051;
		
		CollisionChecker.checkForTileTouch(this.level, this, Direction.LEFT, speed);
		if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, speed)) {
			this.x -= speed;
		}else {
			if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, speed/4)) {
				this.x -= speed/4;
			}else {
				this.y -= 0.5625;
				if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, speed) && onGround) {
					this.x -= speed;
					this.y += 0.46;
				}else {
					this.y += 0.5625;
					if(autoJump) {
						this.y -= 1.4;
						if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, speed) && onGround) {
							this.jump();
						}
						this.y += 1.4;
					}
				}
			}
		}
	}
	
	public void goLeft(boolean autoJump) {
		goLeft(autoJump, 1d);
	}
	
	public void goRight(boolean autoJump, double speed) {
		this.facing = Direction.RIGHT;
		speed *= 0.051;
		
		CollisionChecker.checkForTileTouch(this.level, this, Direction.RIGHT, speed);
		if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, speed)) {
			this.x += speed;
		}else {
			if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, speed/4)) {
				this.x += speed/4;
			}else {
				this.y -= 0.5625;
				if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, speed) && onGround) {
					this.x += speed;
					this.y += 0.46;
				}else {
					this.y += 0.5625;
					if(autoJump) {
						this.y -= 1.4;
						if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, speed) && onGround) {
							this.jump();
						}
						this.y += 1.4;
					}
				}
			}
		}
	}
	
	public void goRight(boolean autoJump) {
		goRight(autoJump, 1d);
	}
	
	public void update() {
		super.update();
		if(this.damageTimer != 0) {
			this.damageTimer--;
		}
	}
	public void damage(double damage, DamageSource source) {
		if(!this.invulnerable) {
			boolean shouldDie = true;
			if(this.health.compareTo(BigDecimal.ZERO) <= 0) {
				shouldDie = false;
			}
			if(this.damageTimer == 0) {
				this.health = this.health.subtract(BigDecimal.valueOf(damage));
				if(this.health.compareTo(BigDecimal.ZERO) <= 0) {
					this.markedForRemoval = true;
					if(shouldDie)
						this.onDied();
				}
				this.damageTimer = damageCooldown;
			}
		}
	}
	public void onDied() {}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.health.doubleValue(), "health");
		sd.setObject(this.maxHealth.doubleValue(), "maxHealth");
		sd.setObject(this.facing, "facing");
		sd.setObject(this.damageTimer, "damageTimer");
		sd.setObject(this.damageCooldown, "damageCooldown");
		sd.setObject(this.shouldRenderHealthBar, "shouldRenderHealthBar");
		sd.setObject(this.invulnerable, "invulnerable");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.maxHealth = BigDecimal.valueOf((double)sd.getObjectDefault("maxHealth", 10));
		this.health = BigDecimal.valueOf((double)sd.getObjectDefault("health", this.maxHealth));
		this.facing = (Direction)sd.getObjectDefault("facing", Direction.RIGHT);
		this.damageTimer = (int)sd.getObjectDefault("damageTimer", 0);
		this.damageCooldown = (int)sd.getObjectDefault("damageCooldown", 10);
		this.shouldRenderHealthBar = (boolean)sd.getObjectDefault("shouldRenderHealthBar", true);
		this.invulnerable = (boolean)sd.getObjectDefault("invulnerable", false);
	}
}