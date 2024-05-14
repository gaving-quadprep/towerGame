package entity;

import java.math.BigDecimal;

import map.Level;
import map.Tile;
import save.SerializedData;

public class LivingEntity extends GravityAffectedEntity {
	public BigDecimal health = BigDecimal.TEN;
	public BigDecimal maxHealth = BigDecimal.TEN;
	public int damageTimer;
	public int damageCooldown = 10;
	public boolean shouldRenderHealthBar = true;
	public LivingEntity(Level level) {
		super(level);
	}
	public boolean canGoTo(int x, int y) {
		int y1 = (int) Math.round(this.y);
		for(int x1 = (int) Math.round(this.x); x1 != x; x1 += (x1 > x ? -1 : 1)) {
			if(!canStandOn(x1, y1)) {
				y1--;
				if(!canStandOn(x1, y1) || Tile.tiles[level.getTileForeground(x, y+1)].isSolid) {
					return false;
				}
			}
		}
		return true;
	}
	public boolean canStandOn(int x, int y) {
		return (!Tile.tiles[level.getTileForeground(x, y)].isSolid) && Tile.tiles[level.getTileForeground(x, y-1)].isSolid;
	}
	public void update() {
		super.update();
		if(this.damageTimer != 0) {
			this.damageTimer--;
		}
	}
	public void damage(double damage) {
		if(this.damageTimer == 0) {
			this.health = this.health.subtract(BigDecimal.valueOf(damage));
			if(this.health.compareTo(BigDecimal.ZERO) <= 0) {
				this.markedForRemoval = true;
				this.onDied();
			}
			this.damageTimer=damageCooldown;
		}
	}
	public void onDied() {}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.health.doubleValue(), "health");
		sd.setObject(this.maxHealth.doubleValue(), "maxHealth");
		sd.setObject(this.damageTimer, "damageTimer");
		sd.setObject(this.damageCooldown, "damageCooldown");
		sd.setObject(this.shouldRenderHealthBar, "shouldRenderHealthBar");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.maxHealth = BigDecimal.valueOf((double)sd.getObjectDefault("maxHealth", 10));
		this.health = BigDecimal.valueOf((double)sd.getObjectDefault("health", this.maxHealth));
		this.damageTimer = (int)sd.getObjectDefault("damageTimer", 0);
		this.damageCooldown = (int)sd.getObjectDefault("damageCooldown", 10);
		this.shouldRenderHealthBar = (boolean)sd.getObjectDefault("shouldRenderHealthBar", true);
	}
}