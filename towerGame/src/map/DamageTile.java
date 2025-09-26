package map;

import java.awt.Rectangle;
import java.math.BigDecimal;

import entity.TileDamageSource;
import entity.Entity;
import entity.LivingEntity;
import towerGame.Player;
import util.Direction;
import util.TilePosition;

public class DamageTile extends Tile {
	public double playerDamage = Double.MAX_VALUE;
	public double entityDamage = 1;
	public boolean entityNeedsToBeIn = false;
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
	public void damage(Level level, LivingEntity entity, int x, int y) {
		if(entity instanceof LivingEntity && !(entity instanceof Player)) {
			((LivingEntity)entity).damage(entityDamage, new TileDamageSource(new TilePosition(x, y)));
		}
		if(entity instanceof Player) {
			((Player)entity).damage(playerDamage, new TileDamageSource(new TilePosition(x, y)));
			if(playerDamage == Double.MAX_VALUE)
				((Player)entity).health = BigDecimal.ZERO;
		}
	}
	@Override
	public void whileTouched(Level level, Entity entity, int x, int y) {
		if (entityNeedsToBeIn)
			if (entity instanceof LivingEntity)
				damage(level, (LivingEntity) entity, x, y);
	}
	@Override
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if (!entityNeedsToBeIn)
			if (entity instanceof LivingEntity)
				damage(level, (LivingEntity) entity, x, y);
	}
}