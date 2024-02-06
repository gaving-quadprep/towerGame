package map;

import java.awt.Rectangle;

public class DamageTile extends Tile {
	public DamageTile(int textureId, boolean isSolid) {
		super(textureId,isSolid);
		Tile.damage_tiles.add(this);
	}
	public DamageTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId,isSolid,hitbox);
		Tile.damage_tiles.add(this);
	}
}