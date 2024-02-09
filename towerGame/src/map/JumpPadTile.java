package map;

import java.awt.Rectangle;

import entity.Entity;
import main.Direction;
import towerGame.Player;

public class JumpPadTile extends Tile {

	public JumpPadTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof Player) {
			((Player) entity).yVelocity += 0.5;
		}
	}
	

}
