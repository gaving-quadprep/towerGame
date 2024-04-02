package map;

import java.awt.Rectangle;

import javax.sound.sampled.Clip;

import entity.Entity;
import entity.FallingTile;
import map.sound.SoundManager;
import towerGame.Player;
import util.Direction;

public class PlateTile extends Tile {

	protected PlateTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof Player) {
			SoundManager.setFile("plate-activate.wav");
			SoundManager.play();
			level.setTileForeground(x, y, 0);
		}
	}

}
