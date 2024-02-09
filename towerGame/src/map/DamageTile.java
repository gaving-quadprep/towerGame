package map;

import java.awt.Rectangle;

import entity.Entity;
import entity.LivingEntity;
import main.Direction;
import towerGame.Player;

public class DamageTile extends Tile {
	public DamageTile(int textureId, boolean isSolid) {
		super(textureId,isSolid);
		Tile.damage_tiles.add(this);
	}
	public DamageTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId,isSolid,hitbox);
		Tile.damage_tiles.add(this);
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		super.onTouch(level, entity, direction, x, y);
		if(entity instanceof LivingEntity) {
			((LivingEntity)entity).health -= 1;
		}
		if(entity instanceof Player) {
			((Player)entity).health = 0;
		}
	}
}