package entity;

import java.math.BigDecimal;

import map.Level;

public class RageSpawn extends Enemy {

	public RageSpawn(Level level) {
		super(level);
		this.maxHealth = BigDecimal.valueOf(5.0D);
		this.health = maxHealth;
		this.attackDamage = 1.5D;
	}
	public String getSprite() {
		return "enemy/ragespawn.png";
	}
}
