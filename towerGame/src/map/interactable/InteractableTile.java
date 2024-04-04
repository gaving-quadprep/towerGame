package map.interactable;

import java.awt.Rectangle;

import gui.TileInteractionGUI;
import map.Level;
import towerGame.TowerGame;

public class InteractableTile extends TileWithData {
	
	public final TileData defaultTileData = null;

	public InteractableTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}

	public InteractableTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	public void onApproachedByPlayer(Level level, int x, int y) {
		TowerGame.showUnique(new TileInteractionGUI());
	}

}
