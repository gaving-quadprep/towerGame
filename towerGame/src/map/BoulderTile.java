package map;

import java.awt.Rectangle;

import entity.FallingBoulder;

public class BoulderTile extends Tile {

	public BoulderTile(int textureId) {
		super(textureId, true);
	}
	public BoulderTile(int textureId, Rectangle rectangle) {
		super(textureId, true, rectangle);
	}
	public void update(Level level, int posX, int posY, boolean foreground) {
		if(foreground&&level.getTileForeground(posX, posY+1)==0) {
			FallingBoulder fb=new FallingBoulder(level);
			fb.setPosition(posX, posY);
			level.addEntity(fb);
			level.setTileForeground(posX,posY,0);
		}
	}
}
