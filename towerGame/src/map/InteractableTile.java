package map;

import java.awt.Rectangle;

import gui.BlockInteractionGUI;
import towerGame.TowerGame;

public class InteractableTile extends Tile {

	public InteractableTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}

	public InteractableTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	public void onApproachedByPlayer(Level level, int x, int y) {
		TowerGame.show(new BlockInteractionGUI());
	}

}
