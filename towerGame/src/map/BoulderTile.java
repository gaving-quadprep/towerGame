package map;

import java.awt.Rectangle;

import entity.FallingTile;

public class BoulderTile extends Tile {

	public BoulderTile(int textureId) {
		super(textureId, true);
	}
	public BoulderTile(int textureId, Rectangle rectangle) {
		super(textureId, true, rectangle);
	}
	public void update(Level level, int x, int y, boolean foreground) {
		if(foreground && level.getTileForeground(x, y+1) == 0) {
			FallingTile fb = new FallingTile(level, id);
			fb.setPosition(x, y);
			level.addEntity(fb);
			level.setTileForeground(x, y, Tile.air.id);
		}
	}
}
