package map;

import java.awt.Rectangle;

import entity.Entity;
import towerGame.Player;
import towerGame.TowerGame;
import util.Direction;

public class ExitTile extends Tile {

	public ExitTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}

	public ExitTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof Player) {
			TowerGame.hasWon = true;
		}
	}

}
