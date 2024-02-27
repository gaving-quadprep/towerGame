package map;

import java.awt.Rectangle;

import entity.Entity;
import towerGame.Player;
import towerGame.TowerGame;
import util.Direction;

public class Checkpoint extends Tile {

	public Checkpoint(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}

	public Checkpoint(int textureId, boolean isSolid, Rectangle rectangle) {
		super(textureId, isSolid, rectangle);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof Player) {
			TowerGame.playerCheckpointX = x;
			TowerGame.playerCheckpointY = y;
		}
	}

}
