package map;

import java.awt.Rectangle;

import entity.Entity;
import entity.FallingTile;
import towerGame.Player;
import util.Direction;

public class LooseTile extends Tile {

	public LooseTile(int textureId) {
		super(textureId, true);
	}

	public LooseTile(int textureId, Rectangle hitbox) {
		super(textureId, true, hitbox);
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(direction == Direction.DOWN && entity instanceof Player) {
			FallingTile fb=new FallingTile(level, id, true);
			fb.setPosition(x, y);
			fb.timeToWaitBeforeFalling = 6;
			level.addEntity(fb);
			level.setTileForeground(x, y, 0);
		}
	}

}
