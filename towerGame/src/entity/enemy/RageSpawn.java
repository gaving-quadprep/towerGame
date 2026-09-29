package entity.enemy;

import java.math.BigDecimal;

import map.Level;
import util.CollisionChecker;

public class RageSpawn extends Enemy {

	public RageSpawn(Level level) {
		super(level);
		maxHealth = BigDecimal.valueOf(5.0D);
		health = maxHealth;
		attackDamage = 1.5D;
		hitbox = CollisionChecker.getHitbox(0, 0, 16, 16);
	}
	@Override
	public String getSprite() {
		return "enemy/ragespawn.png";
	}
}
