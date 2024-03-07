package map;

public class FlowerTile extends Tile {
	int type;
	public FlowerTile(int type) {
		super(type + 16, false);
	}
	public void update(Level level, int posX, int posY, boolean foreground) {
		if(foreground && level.getTileForeground(posX, posY+1)==0) {
			level.setTileForeground(posX, posY, 0);
		}
	}
}
