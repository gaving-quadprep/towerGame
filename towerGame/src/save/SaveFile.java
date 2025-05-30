//TODO this code sucks


package save;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import entity.*;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.panel.TilePanel;
import main.Main;
import map.Level;
import map.Tile;
import map.interactable.TileData;
import map.CustomTile;

public class SaveFile {
	public static void save(Level level, String fileName, boolean compress) throws FileNotFoundException, IOException {
		try {
			level.entity_lock.lock();
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			SerializedData sd = new SerializedData();
			sd.setObject(false, "multiLevel");
			sd.setObject(compress, "GZipCompressed");
			sd.setObject(Main.version,"versionCreatedIn");
			SerializedData sd2 = new SerializedData();
			
			List<SerializedData> entities = new ArrayList<SerializedData>();
			sd2.setObject(entities, "entities");
			level.forEachEntity(false, (e) -> {
				if(!e.markedForRemoval && e!=null) {
					entities.add(e.serialize());
				}
			});
			
			if(level.inLevelEditor) {
				List<SerializedData> customSprites = new ArrayList<SerializedData>();
				sd2.setObject(customSprites, "customSprites");
				
				Set<String> keys = LevelEditor.customSprites.keySet();
				for ( String s : keys) {
					SerializedData sprite = new SerializedData();
					sprite.setObject(s, "name");

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					ImageIO.write(LevelEditor.customSprites.get(s), "png", stream);
					sprite.setObject(stream.toByteArray(), "sprite");
					customSprites.add(sprite);
				}
			}
			
			SerializedData sd3 = new SerializedData();
			sd2.setObject(sd3, "attr");
			sd3.setObject(level.mapTilesBackground, "mapTilesBackground");
			sd3.setObject(level.mapTilesForeground, "mapTilesForeground");
			sd3.addObjects2DSerializableCompact(level.tileDataBackground, "tileDataBackground");
			sd3.addObjects2DSerializableCompact(level.tileDataForeground, "tileDataForeground");
			
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
				sd4.setObject(LevelEditor.playerHealth,"playerHealth");
				sd4.setObject(LevelEditor.playerMana,"playerMana");
				sd4.setObject(0.0D,"playerArmor");
				sd4.setObject(LevelEditor.playerWeapon,"playerWeapon");
				sd4.setObject(LevelEditor.playerSpeed,"playerSpeed");
			}else {
				sd4.setObject(level.player.x,"playerX");
				sd4.setObject(level.player.y,"playerY");
				sd4.setObject(level.player.health.doubleValue(),"playerHealth");
				sd4.setObject(level.player.mana.doubleValue(),"playerMana");
				sd4.setObject(level.player.armor,"playerArmor");
				sd4.setObject(level.player.weapon,"playerWeapon");
				sd4.setObject(level.player.speed,"playerSpeed");
			}
			SerializedData customTiles = new SerializedData();
			for(int i = 0; i < 4096; i++) {
				Tile tile = Tile.customTiles[i];
				if(tile != null && tile instanceof CustomTile ) {
					SerializedData tiledata = ((CustomTile)tile).serialize();
					customTiles.setObject(tiledata, String.valueOf(i));
				}
			}
			sd.setObject(customTiles, "customTiles");
			
			if(compress) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(baos)); 
				oos.writeObject(sd2);
				oos.close();
				sd.setObject(baos.toByteArray(), "level");
				baos.close();
			}else {
				sd.setObject(sd2, "level");
			}
			
			output.writeObject(sd);
			output.close();
		} finally {
			level.entity_lock.unlock();
		}
	}
	
	public static void save(Level level, String fileName) throws FileNotFoundException, IOException {
		save(level, fileName, true);
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void load(Level level, String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		try {
			level.entity_lock.lock();
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(fileName)));
			Object in = input.readObject();
			if(level.inLevelEditor)
				LevelEditorUtils.clearCustomTiles();
			if(in instanceof GameSerializable) { //legacy save format
				GameSerializable gs = (GameSerializable)in;
				level.clearEntities();
				for( SerializedData se : gs.entities) {
					Entity e = Entity.entityRegistry.createByName((String)se.getObject("class"),new Class[] {Level.class}, new Object[] {level});
					if(e != null) {
						e.deserialize(se);
						level.addEntity(e);
					}
				}
				level.sizeX = (int)gs.attr.getObjectDefault("levelSizeX",16);
				level.sizeY = (int)gs.attr.getObjectDefault("levelSizeY",16);
				level.mapTilesBackground = (int[][]) gs.attr.getObjectDefault("mapTilesBackground", new int[level.sizeX][level.sizeY]);
				level.mapTilesForeground = (int[][]) gs.attr.getObjectDefault("mapTilesForeground", new int[level.sizeX][level.sizeY]);
				level.tileDataBackground = new TileData[level.sizeX][level.sizeY];
				level.tileDataForeground = new TileData[level.sizeX][level.sizeY];
				
				
				level.playerStartX = (double)gs.attr.getObjectDefault("playerStartX",4.0D);
				level.playerStartY = (double)gs.attr.getObjectDefault("playerStartY",6.0D);
				if(!level.inLevelEditor) {
					level.player.x = (double)gs.attr.getObjectDefault("playerX",4.0D);
					level.player.y = (double)gs.attr.getObjectDefault("playerY",6.0D);
					level.player.health = BigDecimal.valueOf((double)gs.attr.getObjectDefault("playerHealth",10.0D));
					level.player.mana = BigDecimal.valueOf((double)gs.attr.getObjectDefault("playerMana",15.0D));
					level.player.armor = (double)gs.attr.getObjectDefault("playerArmor",0.0D);
					level.player.setWeapon((int)gs.attr.getObjectDefault("playerWeapon",1));
					level.player.speed = (double)gs.attr.getObjectDefault("playerSpeed",1.0D);
				}
				level.skyColor = (Color)gs.attr.getObjectDefault("skyColor",new Color(98,204,249));
				// gravity was set to 0 before 0.6.3
				// level.gravity=(double)gs.attr.getObjectDefault("gravity",0.007);
				level.gravity = 0.007;
				level.healPlayer = (boolean)gs.attr.getObjectDefault("healPlayer",false);
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
								texture = ImageIO.read(stream);
							}
							if(hitbox.equals(new Rectangle(0,0,16,16))) {
								Tile.tiles[id] = new CustomTile(id+3840, texture, isSolid, doesDamage);
							} else {
								Tile.tiles[id] = new CustomTile(id+3840, texture, isSolid, doesDamage, hitbox);
							}
							((CustomTile)Tile.tiles[id]).name = "";
							Tile.nextCustomTileId++;
							if(level.inLevelEditor) {
								TilePanel.addCustomTileToMenu((CustomTile)Tile.tiles[id], LevelEditor.tilePanel.innerCustomTilePanel);
							}
						}
					}
				}
			}else {
				SerializedData sd = (SerializedData)in;
				SerializedData sd2;
				if((boolean) sd.getObjectDefault("GZipCompressed", false)) {
					byte[] binarray = (byte[]) sd.getObject("level");
					ObjectInputStream stream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(binarray)));
					sd2 = (SerializedData) stream.readObject();
					stream.close();
				}else {
					sd2 = (SerializedData) sd.getObject("level");
				}
				
				List<SerializedData> customSprites = (List<SerializedData>)sd2.getObjectDefault("customSprites", null);
				if (customSprites != null) {
					for (SerializedData cs : customSprites) {
						BufferedImage sprite = null;
						ByteArrayInputStream stream = new ByteArrayInputStream((byte[])cs.getObject("sprite"));
						if(stream!=null) 
							sprite = ImageIO.read(stream);
						
						level.sprites.put((String)cs.getObject("name"), sprite);
					}
				}
				
				level.clearEntities();
				List<SerializedData> entities = (List<SerializedData>)sd2.getObjectDefault("entities", new ArrayList<SerializedData>());
				for( SerializedData se : entities) {
					Entity e = Entity.entityRegistry.createByName((String)se.getObject("class"),new Class[] {Level.class}, new Object[] {level});
					if(e != null) {
						e.deserialize(se);
						level.addEntity(e);
					}
				}
				
				SerializedData attr = (SerializedData) sd2.getObject("attr");
				level.sizeX = (int)attr.getObjectDefault("levelSizeX",15);
				level.sizeY = (int)attr.getObjectDefault("levelSizeY",20);
				level.mapTilesBackground = (int[][]) attr.getObjectDefault("mapTilesBackground", new int[level.sizeX][level.sizeY]);
				level.mapTilesForeground = (int[][]) attr.getObjectDefault("mapTilesForeground", new int[level.sizeX][level.sizeY]);
				level.playerStartX = (double)attr.getObjectDefault("playerStartX",4.0D);
				level.playerStartY = (double)attr.getObjectDefault("playerStartY",6.0D);
				level.tileDataBackground = new TileData[level.sizeX][level.sizeY];
				level.tileDataForeground = new TileData[level.sizeX][level.sizeY];
				SerializedData tdb = (SerializedData)attr.getObject("tileDataBackground");
				SerializedData tdf = (SerializedData)attr.getObject("tileDataForeground");
				SerializedData td;
				if(tdb != null && tdf != null) {
					for(int x=0;x<level.sizeX;x++) {
						if(tdb.getObject(String.valueOf(x)) != null) {
							for(int y=0;y<level.sizeY;y++) {
								td = (SerializedData) ((SerializedData) tdb.getObject(String.valueOf(x))).getObject(String.valueOf(y));
								if(td != null) {
									level.tileDataBackground[x][y] = TileData.registry.createByName((String)td.getObject("class"), new Class[] {}, new Object[] {});
									level.tileDataBackground[x][y].deserialize(td);
								}
							}
						}
						if(tdf.getObject(String.valueOf(x)) != null) {
							for(int y=0;y<level.sizeY;y++) {
								td = (SerializedData) ((SerializedData) tdf.getObject(String.valueOf(x))).getObject(String.valueOf(y));
								if(td != null) {
									level.tileDataForeground[x][y] = TileData.registry.createByName((String)td.getObject("class"), new Class[] {}, new Object[] {});
									level.tileDataForeground[x][y].deserialize(td);
								}
							}
						}
					}
				}
				SerializedData player = (SerializedData) sd2.getObject("player");
				double health =  (double)player.getObjectDefault("playerHealth", 10.0D);
				double mana =  (double)player.getObjectDefault("playerMana", 15.0D);
				double armor = (double)player.getObjectDefault("playerArmor",0.0D);
				double speed = (double)player.getObjectDefault("playerSpeed",1.0D);
				int weapon = (int)player.getObjectDefault("playerWeapon",1);
				
				if(!level.inLevelEditor) {
					level.player.x = (double)player.getObjectDefault("playerX",level.playerStartX);
					level.player.y = (double)player.getObjectDefault("playerY",level.playerStartY);
					level.player.health = BigDecimal.valueOf(health);
					level.player.mana = BigDecimal.valueOf(mana);
					level.player.armor = armor;
					level.player.speed = speed;
					level.player.setWeapon(weapon);
				} else {
					LevelEditor.playerHealth = health;
					LevelEditor.playerMana = mana;
					LevelEditor.playerSpeed = speed;
					LevelEditor.playerWeapon = weapon;
				}
				level.skyColor=(Color)attr.getObjectDefault("skyColor",new Color(98,204,249));
				level.gravity = (double)attr.getObjectDefault("gravity",0.007D);
				// gravity was set to 0 before 0.6.3
				// it was set to 0.000000000000001 (wtf) in versions 0.6.3 and 4
				// no 0.000000000000001 gravity, sorry
				if(level.gravity == 0)
					level.gravity = 0.007;
				if(level.gravity == 0.000000000000001)
					level.gravity = Double.MIN_VALUE; // setting this to 0 causes ladders to be unclimbable
				level.healPlayer=(boolean)attr.getObjectDefault("healPlayer",false);
				SerializedData customTiles = (SerializedData)sd.getObjectDefault("customTiles", null);
				if(customTiles != null) {
					for(int i = 0; i < 4096; i++) {
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
								texture = ImageIO.read(stream);
							}
							if(hitbox == null || hitbox.equals(new Rectangle(0, 0, 16, 16))) {
								CustomTile t = new CustomTile(id + 4096, texture, isSolid, doesDamage);
								t.name = name;
								Tile.tiles[id + 4096] = t;
							} else {
								CustomTile t = new CustomTile(id + 4096, texture, isSolid, doesDamage, hitbox);
								t.name = name;
								Tile.tiles[id + 4096] = t;
							}
							Tile.nextCustomTileId++;
							if(level.inLevelEditor) {
								TilePanel.addCustomTileToMenu((CustomTile)Tile.tiles[id + 4096], LevelEditor.tilePanel.innerCustomTilePanel);
							}
						}
					}
				}
				// reload sprites
				if(!level.inLevelEditor)
					level.player.loadSprites();
				level.reloadTileMap();
				input.close();
			}
		} finally {
			level.entity_lock.unlock();
		}
	}
}
