package map;

import java.awt.Rectangle;

public class DamageTile extends Tile {
	public DamageTile(int id,int textureId, boolean isSolid) {
		super(id,textureId,isSolid);
		Tile.damage_tiles.add(this);
	}
	public DamageTile(int id,int textureId, boolean isSolid, Rectangle hitbox) {
		super(id,textureId,isSolid,hitbox);
		Tile.damage_tiles.add(this);
	}
}