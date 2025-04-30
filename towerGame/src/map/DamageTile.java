package map;

import java.awt.Rectangle;
import java.math.BigDecimal;

import entity.Entity;
import entity.LivingEntity;
import towerGame.Player;
import util.Direction;

public class DamageTile extends Tile {
	private double playerDamage = Double.MAX_VALUE;
	private double entityDamage = 1;
	public DamageTile(int textureId, boolean isSolid, double playerDamage, double entityDamage) {
		super(textureId, isSolid);
		this.playerDamage = playerDamage;
		this.entityDamage = entityDamage;
	}
	public DamageTile(int textureId, boolean isSolid, Rectangle hitbox, double playerDamage, double entityDamage) {
		super(textureId, isSolid, hitbox);
		this.playerDamage = playerDamage;
		this.entityDamage = entityDamage;
	}
	public DamageTile(int textureId, boolean isSolid, Rectangle hitbox) {
		this(textureId, isSolid, hitbox, Double.MAX_VALUE, 1);
	}
	public DamageTile(int textureId, boolean isSolid) {
		this(textureId, isSolid, Double.MAX_VALUE, 1);
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		super.onTouch(level, entity, direction, x, y);
		if(entity instanceof LivingEntity) {
			((LivingEntity)entity).damage(entityDamage);
		}
		if(entity instanceof Player) {
			((Player)entity).damage(playerDamage);
			if(playerDamage == Double.MAX_VALUE)
				((Player)entity).health = BigDecimal.ZERO;
		}
	}
}