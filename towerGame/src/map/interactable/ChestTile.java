package map.interactable;

import java.awt.Rectangle;

import gui.TileInteractionGUI;
import map.Level;
import map.Tile;
import towerGame.TowerGame;

public class ChestTile extends TileWithData {

	public ChestTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		defaultTileData = new ChestTileData(null);
		// TODO Auto-generated constructor stub
	}

	public ChestTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		defaultTileData = new ChestTileData(null);
		// TODO Auto-generated constructor stub
	}
	public void onApproachedByPlayer(Level level, int x, int y) {
		if(this.id != Tile.crate.id)
			TowerGame.showUnique(new TileInteractionGUI());
	}

}
