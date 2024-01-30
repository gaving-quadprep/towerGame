package map;

import java.awt.Rectangle;

import entity.FallingBoulder;

public class BoulderTile extends Tile {

	public BoulderTile(int id, int textureId) {
		super(id, textureId, true);
	}
	public BoulderTile(int id, int textureId, Rectangle rectangle) {
		super(id, textureId, true, rectangle);
	}
	@Override
	public void update(Level level, int posX, int posY, boolean foreground) {
		if(foreground&&level.mapTilesForeground[posX][posY+1]==0) {
			FallingBoulder fb=new FallingBoulder(level);
			fb.x=posX;
			fb.y=posY;
			level.addEntity(fb);
			level.setTileForeground(posX,posY,0);
		}
	}
}
