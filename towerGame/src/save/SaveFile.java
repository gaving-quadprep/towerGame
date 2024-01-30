package save;

import java.io.*;

import entity.*;
import map.Level;

public class SaveFile {
	public static void save(Level level, String fileName) {
		try {
			level.entity_lock.lock();
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			GameSerializable gs = new GameSerializable();
			for ( Entity e : level.entities) {
				if(!e.markedForRemoval && e!=null) {
					gs.entities.add(e);
				}
			}
			gs.mapTilesBackground=level.mapTilesBackground;
			gs.mapTilesForeground=level.mapTilesForeground;
			gs.levelSizeX=level.sizeX;
			gs.levelSizeY=level.sizeY;
			if(level.inLevelEditor) {
				gs.playerX=level.playerStartX;
				gs.playerY=level.playerStartY;
				gs.playerStartX=level.playerStartX;
				gs.playerStartY=level.playerStartY;
				gs.playerHealth=10;
				gs.playerMana=15;
				gs.playerArmor=0;
				gs.playerWeapon=1;
			}else {
				gs.playerX=level.player.x;
				gs.playerY=level.player.y;
				gs.playerStartX=level.playerStartX;
				gs.playerStartY=level.playerStartY;
				gs.playerHealth=level.player.health;
				gs.playerMana=level.player.mana;
				gs.playerArmor=level.player.armor;
				gs.playerWeapon=level.player.weapon;
			}
			gs.skyColor=level.skyColor;
			output.writeObject(gs);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			level.entity_lock.unlock();
		}
	}
	public static void load(Level level, String fileName) {
		try {
			level.entity_lock.lock();
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(fileName)));
			GameSerializable gs = (GameSerializable)input.readObject();
			level.entities.clear();
			for( Entity e : gs.entities) {
				e.level=level;
				level.addEntity(e);
			}
			level.mapTilesBackground=gs.mapTilesBackground;
			level.mapTilesForeground=gs.mapTilesForeground;
			level.sizeX=gs.levelSizeX;
			level.sizeY=gs.levelSizeY;
			if(!level.inLevelEditor) {
				level.player.x=gs.playerX;
				level.player.y=gs.playerY;
				level.player.health=gs.playerHealth;
				level.player.mana=gs.playerMana;
				level.player.armor=gs.playerArmor;
			}
			level.skyColor=gs.skyColor;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			level.entity_lock.unlock();
		}
	}
}
