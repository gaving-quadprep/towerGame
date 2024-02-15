package save;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import entity.*;
import levelEditor.LevelEditor;
import main.Main;
import map.Level;
import map.Tile;
import map.CustomTile;

public class SaveFile {
	public static void save(Level level, String fileName) throws Exception {
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
			gs.attr.setObject(level.gravity,"gravity");
			gs.attr.setObject(level.healPlayer,"healPlayer");
			gs.attr.setObject(Main.version,"versionCreatedIn");
			SerializedData customTiles = new SerializedData();
			for(int i=0;i<256;i++) {
				Tile tile = Tile.customTiles[i];
				if(tile != null && tile instanceof CustomTile) {
					SerializedData tiledata = new SerializedData();
					tiledata.setObject(Tile.customTiles[i].id, "id");
					tiledata.setObject(Tile.customTiles[i].hitbox, "hitbox");
					tiledata.setObject(Tile.customTiles[i].isSolid, "isSolid");
					tiledata.setObject(((CustomTile)Tile.customTiles[i]).doesDamage, "doesDamage");
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					try {
						ImageIO.write(((CustomTile)tile).texture, "png", stream);
					} catch (IOException e) {
						e.printStackTrace();
					}
					tiledata.setObject(stream.toByteArray(), "texture");
					customTiles.setObject(tiledata, String.valueOf(i));
				}
			}
			gs.attr.setObject(customTiles, "customTiles");
			output.writeObject(gs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			level.entity_lock.unlock();
		}
	}
	public static void load(Level level, String fileName) throws Exception {
		try {
			level.entity_lock.lock();
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(fileName)));
			GameSerializable gs = (GameSerializable)input.readObject();
			level.entities.clear();
			for( SerializedData se : gs.entities) {
				Entity e = Entity.entityRegistry.createByName((String)se.getObject("class"),new Class[] {Level.class}, new Object[] {level});
				if(e != null) {
					e.deserialize(se);
					level.addEntity(e);
				}
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
				level.player.setWeapon((int)gs.attr.getObjectDefault("playerWeapon",1));
			}
			level.skyColor=(Color)gs.attr.getObjectDefault("skyColor",new Color(98,204,249));
			level.gravity=(double)gs.attr.getObjectDefault("gravity",0.007D);
			level.healPlayer=(boolean)gs.attr.getObjectDefault("healPlayer",false);
			SerializedData customTiles = (SerializedData)gs.attr.getObjectDefault("customTiles", null);
			if(customTiles != null) {
				for(int i=0;i<255;i++) {
					SerializedData tiledata = (SerializedData)customTiles.getObjectDefault(String.valueOf(i), null);
					if(tiledata != null) {
						int id = (int) tiledata.getObject("id");
						boolean isSolid = (boolean) tiledata.getObjectDefault("isSolid", true);
						boolean doesDamage = (boolean) tiledata.getObjectDefault("doesDamage", false);
						Rectangle hitbox = (Rectangle) tiledata.getObjectDefault("hitbox", new Rectangle(0, 0, 16, 16));
						BufferedImage texture = null;
						ByteArrayInputStream stream = new ByteArrayInputStream((byte[])tiledata.getObject("texture"));
						if(stream!=null) {
							try {
								texture = ImageIO.read(stream);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						if(hitbox.equals(new Rectangle(0,0,16,16))) {
							Tile.tiles[id] = new CustomTile(id, texture, isSolid, doesDamage);
						} else {
							Tile.tiles[id] = new CustomTile(id, texture, isSolid, doesDamage, hitbox);
						}
						Tile.nextCustomTileId++;
						if(level.inLevelEditor) {
							LevelEditor.addCustomTileToMenu((CustomTile)Tile.tiles[id]);
						}
					}
				}
			}
		} finally {
			level.entity_lock.unlock();
		}
	}
}
