package map;

import java.awt.Rectangle;

import entity.Entity;
import map.interactable.BlockedExit;
import map.interactable.ExtendableSpikes;
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
			SoundManager.play("plate-activate.wav");
			level.setTileForeground(x, y, 0);
			new Thread() {
				@Override public void run() {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int x1=0;x1<level.sizeX;x1++) {
						for(int y1=0;y1<level.sizeX;y1++) {
							if(level.getTileForeground(x1, y1) == Tile.activatableSpikes.id) {
								ExtendableSpikes.CustomTileData td = (ExtendableSpikes.CustomTileData)level.getTileDataForeground(x1, y1);
								td.extending = true;
							}
							
						}
					}
					try {
						Thread.sleep(450);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					boolean playSound = false;
					for(int x1=0;x1<level.sizeX;x1++) {
						for(int y1=0;y1<level.sizeX;y1++) {
							if(level.getTileForeground(x1, y1) == Tile.blockedExit.id) {
								playSound = true;
								BlockedExit.CustomTileData td = (BlockedExit.CustomTileData)level.getTileDataForeground(x1, y1);
								td.opening = true;
							}
						}
					}
					if(playSound) {
						SoundManager.play("door_open.wav");
					}
				}
			}.start();
		}
	}

}
