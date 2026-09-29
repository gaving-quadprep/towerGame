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
		int y1 = (int) Math.round(this.y + hitbox.y/16 + hitbox.height/32);
		for(int x1 = (int) Math.round(this.x + hitbox.x/16 + hitbox.width/32); x1 != x; x1 += (x1 > x ? -1 : 1)) {
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
		if(onGround) {
			return true;
		} else {
			int[] positions = CollisionChecker.getTilePositions(level, this, Direction.DOWN, 0);
			if(level.getTileForeground(positions[0], positions[2]) == Tile.jumpTile.id) {
				return true;
			}
			if(level.getTileForeground(positions[1], positions[2]) == Tile.jumpTile.id) {
				return true;
			}
			if(level.getTileForeground(positions[0], positions[3]) == Tile.jumpTile.id) {
				return true;
			}
			if(level.getTileForeground(positions[1], positions[3]) == Tile.jumpTile.id) {
				return true;
			}
			return false;
		}
	}
	public void jump() {
		if(canJump()) {
			yVelocity =- 0.1582F;
			if(CollisionChecker.checkSpecificTile(level, this, Direction.DOWN, 0, Tile.jumpPad) || CollisionChecker.checkSpecificTile(level, this, Direction.UP, 0, Tile.jumpPad)) {
				yVelocity -= 0.0342F;
			}
		}
	}

	public void goLeft(boolean autoJump, double speed) {
		facing = Direction.LEFT;
		speed *= 0.051;

		CollisionChecker.checkForTileTouch(level, this, Direction.LEFT, speed);
		if(!CollisionChecker.checkTile(level, this, Direction.LEFT, speed)) {
			x -= speed;
		} else if(!CollisionChecker.checkTile(level, this, Direction.LEFT, speed/4)) {
			x -= speed/4;
		}else {
			y -= 0.5625;
			if(!CollisionChecker.checkTile(level, this, Direction.LEFT, speed) && onGround) {
				x -= speed;
				y += 0.46;
			}else {
				y += 0.5625;
				if(autoJump) {
					y -= 1.4;
					if(!CollisionChecker.checkTile(level, this, Direction.LEFT, speed) && onGround) {
						jump();
					}
					y += 1.4;
				}
			}
		}
	}

	public void goLeft(boolean autoJump) {
		goLeft(autoJump, 1d);
	}

	public void goRight(boolean autoJump, double speed) {
		facing = Direction.RIGHT;
		speed *= 0.051;

		CollisionChecker.checkForTileTouch(level, this, Direction.RIGHT, speed);
		if(!CollisionChecker.checkTile(level, this, Direction.RIGHT, speed)) {
			x += speed;
		} else if(!CollisionChecker.checkTile(level, this, Direction.RIGHT, speed/4)) {
			x += speed/4;
		}else {
			y -= 0.5625;
			if(!CollisionChecker.checkTile(level, this, Direction.RIGHT, speed) && onGround) {
				x += speed;
				y += 0.46;
			}else {
				y += 0.5625;
				if(autoJump) {
					y -= 1.4;
					if(!CollisionChecker.checkTile(level, this, Direction.RIGHT, speed) && onGround) {
						jump();
					}
					y += 1.4;
				}
			}
		}
	}

	public void goRight(boolean autoJump) {
		goRight(autoJump, 1d);
	}

	@Override
	public void update() {
		super.update();
		if(damageTimer != 0) {
			damageTimer--;
		}
	}
	public void damage(double damage, DamageSource source) {
		if(!invulnerable) {
			boolean shouldDie = true;
			if(health.compareTo(BigDecimal.ZERO) <= 0) {
				shouldDie = false;
			}
			if(damageTimer == 0) {
				health = health.subtract(BigDecimal.valueOf(damage));
				if(health.compareTo(BigDecimal.ZERO) <= 0) {
					markedForRemoval = true;
					if(shouldDie)
						onDied();
				}
				damageTimer = damageCooldown;
			}
		}
	}
	public void onDied() {}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(health.doubleValue(), "health");
		sd.setObject(maxHealth.doubleValue(), "maxHealth");
		sd.setObject(facing, "facing");
		sd.setObject(damageTimer, "damageTimer");
		sd.setObject(damageCooldown, "damageCooldown");
		sd.setObject(shouldRenderHealthBar, "shouldRenderHealthBar");
		sd.setObject(invulnerable, "invulnerable");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		maxHealth = BigDecimal.valueOf((double)sd.getObjectDefault("maxHealth", 10));
		health = BigDecimal.valueOf((double)sd.getObjectDefault("health", maxHealth));
		facing = (Direction)sd.getObjectDefault("facing", Direction.RIGHT);
		damageTimer = (int)sd.getObjectDefault("damageTimer", 0);
		damageCooldown = (int)sd.getObjectDefault("damageCooldown", 10);
		shouldRenderHealthBar = (boolean)sd.getObjectDefault("shouldRenderHealthBar", true);
		invulnerable = (boolean)sd.getObjectDefault("invulnerable", false);
	}
}