package map;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LifeTile extends Tile {
	// Change these for custom rules like highlife
	private static final Set<Integer> S = new HashSet<Integer>(Arrays.asList( 
            new Integer[] { 2, 3 }));
	private static final Set<Integer> B = new HashSet<Integer>(Arrays.asList( 
            new Integer[] { 3 }));
	public LifeTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}
	public int getNeighbors(Level level, int x, int y, boolean foreground) {
		int neighbors = 0;
		if(level.getTile(x-1, y-1, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x, y-1, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x+1, y-1, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x-1, y, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x+1, y, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x-1, y+1, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x, y+1, foreground) == Tile.alive.id) neighbors++;
		if(level.getTile(x+1, y+1, foreground) == Tile.alive.id) neighbors++;
		return neighbors;
	}
	public void update(Level level, int x, int y, boolean foreground) {
		int neighbors = this.getNeighbors(level, x, y, foreground);
		if(this.id == Tile.dead.id) {
			if(B.contains(neighbors)) {
				level.setTileQueued(x, y, Tile.alive.id, foreground);
			}
		}
		if(this.id == Tile.alive.id) {
			if(!S.contains(neighbors)) {
				level.setTileQueued(x, y, Tile.dead.id, foreground);
			}
		}
	}

}
