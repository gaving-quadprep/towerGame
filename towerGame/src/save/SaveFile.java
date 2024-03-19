package save;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
			SerializedData sd = new SerializedData();
			sd.setObject(false, "multiLevel");
			sd.setObject(Main.version,"versionCreatedIn");
			SerializedData sd2 = new SerializedData();
			sd.setObject(sd2, "level");
			List<SerializedData> entities = new ArrayList<SerializedData>();
			sd2.setObject(entities, "entities");
			for ( Entity e : level.entities) {
				if(!e.markedForRemoval && e!=null) {
					entities.add(e.serialize());
				}
			}
			SerializedData sd3 = new SerializedData();
			sd2.setObject(sd3, "attr");
			sd3.setObject(level.mapTilesBackground, "mapTilesBackground");
			sd3.setObject(level.mapTilesForeground, "mapTilesForeground");
			sd3.setObject(level.sizeX,"levelSizeX");
			sd3.setObject(level.sizeY,"levelSizeY");
			sd3.setObject(level.playerStartX,"playerStartX");
			sd3.setObject(level.playerStartY,"playerStartY");
			sd3.setObject(level.skyColor,"skyColor");
			sd3.setObject(level.gravity,"gravity");
			sd3.setObject(level.healPlayer,"healPlayer");
			SerializedData sd4 = new SerializedData();
			sd2.setObject(sd4, "player");
			if(level.inLevelEditor) {
				sd4.setObject(level.playerStartX,"playerX");
				sd4.setObject(level.playerStartY,"playerY");
				sd4.setObject(10.0D,"playerHealth");
				sd4.setObject(15.0D,"playerMana");
				sd4.setObject(0.0D,"playerArmor");
				sd4.setObject(1,"playerWeapon");
			}else {
				sd4.setObject(level.player.x,"playerX");
				sd4.setObject(level.player.y,"playerY");
				sd4.setObject(level.player.health.doubleValue(),"playerHealth");
				sd4.setObject(level.player.mana,"playerMana");
				sd4.setObject(level.player.armor,"playerArmor");
				sd4.setObject(level.player.weapon,"playerWeapon");
			}
			SerializedData customTiles = new SerializedData();
			for(int i=0;i<4096;i++) {
				Tile tile = Tile.customTiles[i];
				if(tile != null && tile instanceof CustomTile ) {
					SerializedData tiledata = ((CustomTile)tile).serialize();
					customTiles.setObject(tiledata, String.valueOf(i));
				}
			}
			sd.setObject(customTiles, "customTiles");
			output.writeObject(sd);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			level.entity_lock.unlock();
		}
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void load(Level level, String fileName) throws Exception {
		try {
			level.entity_lock.lock();
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(fileName)));
			Object in = input.readObject();
			if(level.inLevelEditor)
				LevelEditor.gamePanel.clearCustomTiles();
			if(in instanceof GameSerializable) {
				GameSerializable gs = (GameSerializable)in;
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
					level.player.health=BigDecimal.valueOf((double)gs.attr.getObjectDefault("playerHealth",10.0D));
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
								Tile.tiles[id] = new CustomTile(id+3840, texture, isSolid, doesDamage);
							} else {
								Tile.tiles[id] = new CustomTile(id+3840, texture, isSolid, doesDamage, hitbox);
							}
							((CustomTile)Tile.tiles[id]).name = "";
							Tile.nextCustomTileId++;
							if(level.inLevelEditor) {
								LevelEditor.addCustomTileToMenu((CustomTile)Tile.tiles[id]);
							}
						}
					}
				}
			}else {
				SerializedData sd = (SerializedData)in;
				SerializedData sd2 = (SerializedData) sd.getObject("level");
				level.entities.clear();
				List<SerializedData> entities = (List<SerializedData>)sd2.getObjectDefault("entities", new ArrayList<SerializedData>());
				for( SerializedData se : entities) {
					Entity e = Entity.entityRegistry.createByName((String)se.getObject("class"),new Class[] {Level.class}, new Object[] {level});
					if(e != null) {
						e.deserialize(se);
						level.addEntity(e);
					}
				}
				SerializedData attr = (SerializedData) sd2.getObject("attr");
				level.sizeX=(int)attr.getObjectDefault("levelSizeX",15);
				level.sizeY=(int)attr.getObjectDefault("levelSizeY",20);
				level.mapTilesBackground=(int[][]) attr.getObjectDefault("mapTilesBackground", new int[level.sizeX][level.sizeY]);
				level.mapTilesForeground=(int[][]) attr.getObjectDefault("mapTilesForeground", new int[level.sizeX][level.sizeY]);
				level.playerStartX=(double)attr.getObjectDefault("playerStartX",4.0D);
				level.playerStartY=(double)attr.getObjectDefault("playerStartY",6.0D);
				if(!level.inLevelEditor) {
					SerializedData player = (SerializedData) sd2.getObject("player");
					level.player.x=(double)player.getObjectDefault("playerX",level.playerStartX);
					level.player.y=(double)player.getObjectDefault("playerY",level.playerStartY);
					level.player.health=BigDecimal.valueOf((double)player.getObjectDefault("playerHealth",10.0D));
					level.player.mana=(double)player.getObjectDefault("playerMana",15.0D);
					level.player.armor=(double)player.getObjectDefault("playerArmor",0.0D);
					level.player.setWeapon((int)player.getObjectDefault("playerWeapon",1));
				}
				level.skyColor=(Color)attr.getObjectDefault("skyColor",new Color(98,204,249));
				level.gravity=(double)attr.getObjectDefault("gravity",0.007D);
				level.healPlayer=(boolean)attr.getObjectDefault("healPlayer",false);
				SerializedData customTiles = (SerializedData)sd.getObjectDefault("customTiles", null);
				if(customTiles != null) {
					for(int i=0;i<4096;i++) {
						SerializedData tiledata = (SerializedData)customTiles.getObjectDefault(String.valueOf(i), null);
						if(tiledata != null) {
							int id = (int) tiledata.getObject("id");
							boolean isSolid = (boolean) tiledata.getObjectDefault("isSolid", true);
							boolean doesDamage = (boolean) tiledata.getObjectDefault("doesDamage", false);
							String name = (String) tiledata.getObjectDefault("name", "");
							Rectangle hitbox = (Rectangle) tiledata.getObject("hitbox");
							BufferedImage texture = null;
							ByteArrayInputStream stream = new ByteArrayInputStream((byte[])tiledata.getObject("texture"));
							if(stream!=null) {
								try {
									texture = ImageIO.read(stream);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							if(hitbox == null || hitbox.equals(new Rectangle(0, 0, 16, 16))) {
								CustomTile t = new CustomTile(id+4096, texture, isSolid, doesDamage);
								t.name = name;
								Tile.tiles[id+4096] = t;
							} else {
								CustomTile t = new CustomTile(id+4096, texture, isSolid, doesDamage, hitbox);
								t.name = name;
								Tile.tiles[id+4096] = t;
							}
							Tile.nextCustomTileId++;
							if(level.inLevelEditor) {
								LevelEditor.addCustomTileToMenu((CustomTile)Tile.tiles[id+4096]);
							}
						}
					}
				}
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			level.entity_lock.unlock();
		}
	}
}
