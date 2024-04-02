package map;

import java.awt.Rectangle;

import javax.sound.sampled.Clip;

import entity.Entity;
import entity.FallingTile;
import map.interactable.BaseTileData;
import map.interactable.BlockedExit;
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
			new Thread() {
				@Override public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					boolean playSound = false;
					for(int x1=0;x1<level.sizeX;x1++) {
						for(int y1=0;y1<level.sizeX;y1++) {
							if(level.getTileForeground(x1, y1) == Tile.blockedExit.id) {
								playSound = true;
								BlockedExit.TileData td = (BlockedExit.TileData)level.getTileDataForeground(x1, y1);
								td.opening = true;
							}
						}
					}
					if(playSound) {
						SoundManager.setFile("door_open.wav");
						SoundManager.play();
					}
				}
			}.start();
		}
	}

}
