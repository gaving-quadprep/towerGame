package map;

public class FlowerTile extends Tile {
	int type;
	public FlowerTile(int type) {
		super(type + 16, false);
	}
	public void update(Level level, int x, int y, boolean foreground) {
		if(foreground && level.getTileForeground(x, y+1) == 0) {
			level.destroy(x, y);
		}
	}
}
