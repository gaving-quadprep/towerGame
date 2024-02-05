package save;

import java.awt.Color;
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
					gs.entities.add(e.serialize());
				}
			}
			gs.attr.setObject(level.mapTilesBackground, "mapTilesBackground");
			gs.attr.setObject(level.mapTilesForeground, "mapTilesForeground");
			gs.attr.setObject(level.sizeX,"levelSizeX");
			gs.attr.setObject(level.sizeY,"levelSizeY");
			gs.attr.setObject(level.playerStartX,"playerStartX");
			gs.attr.setObject(level.playerStartY,"playerStartY");
			if(level.inLevelEditor) {
				gs.attr.setObject(level.playerStartX,"playerX");
				gs.attr.setObject(level.playerStartY,"playerY");
				gs.attr.setObject(10.0D,"playerHealth");
				gs.attr.setObject(15.0D,"playerMana");
				gs.attr.setObject(0.0D,"playerArmor");
				gs.attr.setObject(1,"playerWeapon");
			}else {
				gs.attr.setObject(level.player.x,"playerX");
				gs.attr.setObject(level.player.y,"playerY");
				gs.attr.setObject(level.player.health,"playerHealth");
				gs.attr.setObject(level.player.mana,"playerMana");
				gs.attr.setObject(level.player.armor,"playerArmor");
				gs.attr.setObject(level.player.weapon,"playerWeapon");
			}
			gs.attr.setObject(level.skyColor,"skyColor");
			output.writeObject(gs);
		} catch (Exception e) {
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
			for( SerializedData se : gs.entities) {
				Entity e = EntityRegistry.createEntityByName((String)se.getObject("class"), level);
				e.deserialize(se);
				level.addEntity(e);
			}
			level.sizeX=(int)gs.attr.getObjectDefault("levelSizeX",16);
			level.sizeY=(int)gs.attr.getObjectDefault("levelSizeY",16);
			level.mapTilesBackground=(int[][]) gs.attr.getObjectDefault("mapTilesBackground", new int[level.sizeX][level.sizeY]);
			level.mapTilesForeground=(int[][]) gs.attr.getObjectDefault("mapTilesForeground", new int[level.sizeX][level.sizeY]);
			level.playerStartX=(double)gs.attr.getObjectDefault("playerStartX",4.0D);
			level.playerStartY=(double)gs.attr.getObjectDefault("playerStartY",6.0D);
			if(!level.inLevelEditor) {
				level.player.x=(double)gs.attr.getObjectDefault("playerX",4.0D);
				level.player.y=(double)gs.attr.getObjectDefault("playerY",6.0D);
				level.player.health=(double)gs.attr.getObjectDefault("playerHealth",10.0D);
				level.player.mana=(double)gs.attr.getObjectDefault("playerMana",15.0D);
				level.player.armor=(double)gs.attr.getObjectDefault("playerArmor",0.0D);
				level.player.weapon=(int)gs.attr.getObjectDefault("playerWeapon",1);
			}
			level.skyColor=(Color)gs.attr.getObjectDefault("skyColor",new Color(98,204,249));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			level.entity_lock.unlock();
		}
	}
}
