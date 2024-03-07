package map.interactable;

import java.awt.Rectangle;

import map.Tile;

public abstract class TileWithData extends Tile {
	
	public TileData defaultTileData;

	public TileWithData(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}

	public TileWithData(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	
	public TileWithData(int id, int textureId, boolean isSolid) {
		super(id, textureId, isSolid);
		// TODO Auto-generated constructor stub
	}
	
	public TileWithData(int id, int textureId, boolean isSolid, Rectangle hitbox) {
		super(id, textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}

}
