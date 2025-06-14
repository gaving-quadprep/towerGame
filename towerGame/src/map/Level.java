package map;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import entity.Entity;
import main.Main;
import main.WorldRenderer;
import map.interactable.TileData;
import map.interactable.TileWithData;
import sound.SoundManager;
import towerGame.EventHandler;
import towerGame.Player;
import util.SuperClassFinder;
import util.TilePosition;

public class Level {
	private static class QueuedTile {
		int x;
		int y;
		boolean foreground;
		int tile;
		TileData td;
		QueuedTile(int x, int y, boolean foreground, int tile) {
			this.x = x;
			this.y = y;
			this.foreground = foreground;
			this.tile = tile;
		}
		QueuedTile(int x, int y, boolean foreground, int tile, TileData td) {
			this(x, y, foreground, tile);
			this.td = td;
		}
	}
	
	private static SuperClassFinder<Entity> scf = new SuperClassFinder<Entity>(Entity.class);
	
	public int sizeX;
	public int sizeY;
	public int mapTilesForeground[][];
	public int mapTilesBackground[][];
	public TileData[][] tileDataForeground;
	public TileData[][] tileDataBackground;
	public BufferedImage tilemap;
	public BufferedImage tilemap_dark;
	public BufferedImage[] tiles = new BufferedImage[256];
	public BufferedImage[] tiles_dark = new BufferedImage[256];
	public RescaleOp bg_tint;
	private List<Entity> entities = new ArrayList<Entity>();
	private Map<Class<? extends Entity>, List<Entity>> entitiesByClass = new HashMap<Class<? extends Entity>, List<Entity>>();
	private List<Entity> entityQueue = new ArrayList<Entity>();
	private List<QueuedTile> tileQueue = new ArrayList<QueuedTile>();
	public HashMap<String,BufferedImage> sprites = new HashMap<String,BufferedImage>();
	public Player player;
	public double playerStartX = 4;
	public double playerStartY = 6;
	public final ReentrantLock entity_lock = new ReentrantLock();
	public double cameraX;
	public double cameraY;
	public Color skyColor = new Color(98,204,249);
	public boolean inLevelEditor = false;
	public double gravity = 0.007;
	public boolean healPlayer = true;
	private Random random = new Random(System.currentTimeMillis());
	public Level(int sizeX, int sizeY) {
		this.mapTilesForeground = new int[sizeX][sizeY];
		this.mapTilesBackground = new int[sizeX][sizeY];
		this.tileDataForeground = new TileData[sizeX][sizeY];
		this.tileDataBackground = new TileData[sizeX][sizeY];
		bg_tint = new RescaleOp(0.87f, 0f, null);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		reloadTileMap();
	}
	public Level(int sizeX, int sizeY, boolean inLevelEditor) {
		this(sizeX, sizeY);
		this.inLevelEditor = inLevelEditor;
		
		for (Class<? extends Entity> clazz : Entity.entityRegistry.getValues()) {
			for (Class<? extends Entity> superClass : scf.getSuperclasses(clazz)) {
				entitiesByClass.putIfAbsent(superClass, new ArrayList<Entity>());
			}
		}
	}
	public boolean outOfBounds(int x, int y) {
		return (x < 0 | x >= this.sizeX | y < 0 | y >= this.sizeY);
	}
	public void rescaleTiles() {
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < 16; y++) {
				int frameX = x * 16;
				int frameY = y * 16;
				BufferedImage img = new BufferedImage(Main.tileSize, Main.tileSize, BufferedImage.TYPE_4BYTE_ABGR);
				BufferedImage img_dark = new BufferedImage(Main.tileSize, Main.tileSize, BufferedImage.TYPE_4BYTE_ABGR);
				img.getGraphics().drawImage(tilemap, 0, 0, Main.tileSize, Main.tileSize, frameX, frameY, frameX+16, frameY+16, null);
				img_dark.getGraphics().drawImage(tilemap_dark, 0, 0, Main.tileSize, Main.tileSize, frameX, frameY, frameX+16, frameY+16, null);
				tiles[(y*16)+x] = img;
				tiles_dark[(y*16)+x] = img_dark;
			}
		}
	}
	
	public void reloadTileMap() {
		tilemap = getSprite("tilemap.png");
		tilemap_dark = bg_tint.filter(tilemap, null);
		rescaleTiles();
	}
	
	public void update(EventHandler eventHandler) {
		if(!inLevelEditor) {
			for(int x = 0; x < this.sizeX; x++) {
				for(int y = 0; y < this.sizeY; y++) {
					if(mapTilesBackground[x][y]!=0) {
						Tile.tiles[mapTilesBackground[x][y]].update(this, x, y, false);
					}
					if(mapTilesForeground[x][y]!=0) {
						Tile.tiles[mapTilesForeground[x][y]].update(this, x, y, true);
					}
				}
			}
		}
		try {
			entity_lock.lock();
			if(!inLevelEditor) {
				for (Entity entity : this.entities) {
					if (entity != null) {
						entity.update();
					}
				}
				if(this.player != null) {
					this.player.update(eventHandler);
				}
			}
			for (Entity e : entityQueue) {
				entities.add(e);
				for (Class<? extends Entity> clazz : scf.getSuperclasses(e.getClass())) {
					List<Entity> classList = entitiesByClass.get(clazz);
					if (classList != null) {
						classList.add(e);
					}
				}
			}
			entityQueue.clear();
			
			Iterator<Entity> i = entities.iterator();
			while(i.hasNext()) {
				Entity e = i.next();
				if (e.markedForRemoval) {
					i.remove();
					for (Class<? extends Entity> clazz : scf.getSuperclasses(e.getClass())) {
						entitiesByClass.get(clazz).remove(e);
					}
				}
			}
		} finally {
			entity_lock.unlock();
		}
		for(QueuedTile qt : tileQueue) {
			setTile(qt.x, qt.y, qt.tile, qt.foreground);
			if(qt.td != null)
				setTileData(qt.x, qt.y, qt.td, qt.foreground);
		}
		tileQueue.clear();
	}
	public void update() {
		this.update(null);
	}
	public void render(WorldRenderer wr) {
		wr.level = this;
		if(player != null) {
			if(player.x-3 > 0)
				if(player.x < cameraX+3)
					cameraX = player.x-3;
			if(player.x-16 < sizeX-20)
				if(player.x > cameraX+16)
					cameraX = player.x-16;
			if(player.y < cameraY+3)
				cameraY = player.y-3;
			if(player.y > cameraY+11)
				cameraY = player.y-11;
		}
		for(int x = Math.max(0, (int)cameraX); x < Math.min((int)cameraX + Main.width + 2, this.sizeX); x++) {
			for(int y = Math.max(0, (int)cameraY); y < Math.min((int)cameraY + Main.height + 2, this.sizeY); y++) {
				if(mapTilesBackground[x][y] != 0) {
					if(Tile.tiles[mapTilesBackground[x][y]] != null)
						Tile.tiles[mapTilesBackground[x][y]].render(this, wr, x, y, false);
				}
				if(mapTilesForeground[x][y] != 0) {
					if(Tile.tiles[mapTilesForeground[x][y]] != null)
						Tile.tiles[mapTilesForeground[x][y]].render(this, wr, x, y, true);
				}
			}
		}
		try {
			entity_lock.lock();
			for (Entity entity : this.entities) {
				entity.render(wr);
			}
		} finally {
			entity_lock.unlock();
		}
		if(this.player != null) {
			this.player.render(wr);
		}
	}
	public void renderBackgroundOnly(WorldRenderer wr) {
		for(int x = Math.max(0, (int)cameraX); x < Math.min((int)cameraX + Main.width + 1,this.sizeX); x++) {
			for(int y = Math.max(0, (int)cameraY); y < Math.min((int)cameraY + Main.height + 1,this.sizeY); y++) {
				if(mapTilesBackground[x][y] != 0) {
					Tile.tiles[mapTilesBackground[x][y]].render(this, wr, x, y, false);
				}
			}
		}
	}
	
	public int getTileBackground(int x,int y) {
		if(outOfBounds(x, y))
			return 0;
		return mapTilesBackground[x][y];
	}
	
	public int getTileForeground(int x,int y) {
		if(outOfBounds(x, y))
			return 0;
		return mapTilesForeground[x][y];
	}
	
	public int getTile(int x, int y, boolean foreground) {
		if(foreground)
			return getTileForeground(x, y);
		else
			return getTileBackground(x, y);
	}
	
	public TileData getTileDataBackground(int x, int y) {
		if(outOfBounds(x, y))
			return ((TileWithData)Tile.tiles[getTileBackground(x, y)]).defaultTileData.clone();
		if(tileDataBackground[x][y] == null) {
			TileData defaultTileData = ((TileWithData)Tile.tiles[getTileBackground(x, y)]).defaultTileData;
			if(defaultTileData != null)
				tileDataBackground[x][y] = defaultTileData.clone();
		}
		TileData tileData = tileDataBackground[x][y];
		return tileData;
	}
	
	public TileData getTileDataForeground(int x, int y) {
		if(outOfBounds(x, y))
			return ((TileWithData)Tile.tiles[getTileForeground(x, y)]).defaultTileData.clone();
		if(tileDataForeground[x][y] == null) {
			TileData defaultTileData = ((TileWithData)Tile.tiles[getTileForeground(x, y)]).defaultTileData;
			if(defaultTileData != null)
				tileDataForeground[x][y] = defaultTileData.clone();
		}
		TileData tileData = tileDataForeground[x][y];
		return tileData;
	}
	
	public TileData getTileData(int x, int y, boolean foreground) {
		if(foreground)
			return getTileDataForeground(x, y);
		else
			return getTileDataBackground(x, y);
	}
	
	public void setTileBackground(int x, int y, int tile) {
		if(outOfBounds(x, y))
			return;
		mapTilesBackground[x][y]=tile;
		if(Tile.tiles[tile] instanceof TileWithData) {
			TileData td = ((TileWithData)Tile.tiles[tile]).defaultTileData;
			tileDataBackground[x][y] = (td != null ? td.clone() : null);
		}
	}
	
	public void setTileForeground(int x, int y, int tile) {
		if(outOfBounds(x, y))
			return;
		mapTilesForeground[x][y]=tile;
		if(Tile.tiles[tile] instanceof TileWithData) {
			TileData td = ((TileWithData)Tile.tiles[tile]).defaultTileData;
			tileDataForeground[x][y] = (td != null ? td.clone() : null);
		}
	}
	
	public void setTile(int x, int y, int tile, boolean foreground) {
		if(foreground)
			setTileForeground(x, y, tile);
		else
			setTileBackground(x, y, tile);
	}
	
	public void setTileQueued(int x, int y, int tile, boolean foreground) {
		tileQueue.add(new QueuedTile(x, y, foreground, tile));
	}
	
	public void setTileQueued(int x, int y, int tile, boolean foreground, TileData td) {
		tileQueue.add(new QueuedTile(x, y, foreground, tile, td));
	}

	public void setTileDataBackground(int x, int y, TileData td) {
		if(outOfBounds(x, y)) {	
			return;
		}
		tileDataBackground[x][y] = td.clone();
	}
	
	public void setTileDataForeground(int x, int y, TileData td) {
		if(outOfBounds(x, y)) {	
			return;
		}
		tileDataForeground[x][y] = td.clone();
	}
	
	public void setTileData(int x, int y, TileData td, boolean foreground) {
		if(foreground)
			setTileDataForeground(x, y, td);
		else
			setTileDataBackground(x, y, td);
	}
	
	public void destroy(int x, int y, boolean playSound) {
		Tile.tiles[mapTilesForeground[x][y]].onDestroyed(this, x, y);
		setTileForeground(x, y, 0);
		if(playSound)
			SoundManager.play("boulder.wav", 0);
	}
	public void destroy(int x, int y) {
		destroy(x, y, false);
	}
	
	public void destroyIfCracked(int x, int y, boolean playSound) {
		if(Tile.isCracked(getTileForeground(x, y)))
			destroy(x, y, playSound);
	}
	public void destroyIfCracked(int x, int y) {
		destroyIfCracked(x, y, false);
	}
	
	public void floodFill(int x, int y, int setTile, boolean foreground) {
		Queue<TilePosition> q = new ArrayDeque<TilePosition>();
		q.offer(new TilePosition(x, y));
		int tile = this.getTile(x, y, foreground);
		while(!q.isEmpty()) {
			TilePosition p = q.poll();
			if( !outOfBounds(p.x, p.y)) {
				if(tile == setTile) return;
				int t = this.getTile(p.x, p.y, foreground);
				if(t == tile) {
					this.setTile(p.x, p.y, setTile, foreground);
					if(this.getTile(p.x - 1, p.y, foreground) == tile)
						q.offer(new TilePosition(p.x - 1, p.y));
					if(this.getTile(p.x + 1, p.y, foreground) == tile)
						q.offer(new TilePosition(p.x + 1, p.y));
					if(this.getTile(p.x, p.y - 1, foreground) == tile)
						q.offer(new TilePosition(p.x, p.y - 1));
					if(this.getTile(p.x, p.y + 1, foreground) == tile)
						q.offer(new TilePosition(p.x, p.y + 1));
				}
			}
		}
	}
	public void floodFillSlow(int x, int y, int setTile, boolean foreground) {
		new Thread() {
			@Override public void run() {
				Queue<TilePosition> q = new ArrayDeque<TilePosition>();
				q.offer(new TilePosition(x, y));
				int tile = getTile(x, y, foreground);
				while(!q.isEmpty()) {
					try {
						Thread.sleep(0, 100000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					TilePosition p = q.poll();
					if(!outOfBounds(p.x, p.y)) {
						if(tile == setTile) return;
						int t = getTile(p.x, p.y, foreground);
						if(t == tile) {
							setTile(p.x, p.y, setTile, foreground);
							if(getTile(p.x - 1, p.y, foreground) == tile)
								q.offer(new TilePosition(p.x - 1, p.y));
							if(getTile(p.x + 1, p.y, foreground) == tile)
								q.offer(new TilePosition(p.x + 1, p.y));
							if(getTile(p.x, p.y - 1, foreground) == tile)
								q.offer(new TilePosition(p.x, p.y - 1));
							if(getTile(p.x, p.y + 1, foreground) == tile)
								q.offer(new TilePosition(p.x, p.y + 1));
						}
					}
				}
			}
		}.start();
	}
	
	public void addEntity(Entity entity) {
		entity.level = this;
		entity.id = this.random.nextLong();
		if (!entity.customSprite)
			entity.loadSprites();
		this.entityQueue.add(entity);
	}
	
	public void setPlayer(Player player) {
		player.level = this;
		player.loadSprites();
		this.player = player;
		centerCameraOnPlayer();
	}
	public void centerCameraOnPlayer() {
		cameraX = (player.x - 10);
		cameraY = (player.y - 9);
		if(cameraX > sizeX - Main.width)
			cameraX = sizeX - Main.width;
		if(cameraX < 0)
			cameraX = 0;
		if(cameraY < 0)
			cameraY = 0;
		if(cameraY > sizeY - Main.height)
			cameraY = sizeY - Main.height;
	}
	public Player getPlayer() {
		return this.player;
	}
	public BufferedImage getSprite(String spriteName) {
		if(spriteName != "") {
			if(!this.sprites.containsKey(spriteName)) {
				try {
					this.sprites.put(spriteName, ImageIO.read(getClass().getResourceAsStream("/sprites/"+spriteName)));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to load " + spriteName + " sprite", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			return this.sprites.get(spriteName);
		}
		return null;
		
	}
	
	public void clearSprites() {
		this.sprites.clear();
	}
	public List<Entity> getAllEntities() {
		List<Entity> e2 = new ArrayList<Entity>(entities.size() + 1);
		e2.addAll(entities);
		e2.add(player);
		return e2;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> getAllEntitiesOfType(Class<T> type, boolean includePlayer) {
		if (type == Player.class) {
			List<T> playerList = new ArrayList<T>();
			playerList.add((T)player);
			return playerList;
		}
		List<T> entityList = (List<T>) entitiesByClass.get(type);
		List<T> newList = new ArrayList<T>(entityList);
		if (includePlayer)
			if(type.isInstance(player))
				newList.add((T)player);
		return newList;
	}
	
	public void forEachEntity(boolean includePlayer, Consumer<Entity> function) {
		for (Entity e : entities) {
			function.accept(e);
		}
		if(includePlayer)
			function.accept(player);
	}
	
	public <T extends Entity> void forEachEntityOfType(Class<T> type, boolean includePlayer, Consumer<T> function) {
		for (Entity e : entitiesByClass.get(type)) {
			function.accept((T)e);
		}
		if(includePlayer)
			if(type.isInstance(player))
				function.accept((T) player);
	}
	
	public void clearEntities() { 
		entities.clear();
		for (List<Entity> e : entitiesByClass.values()) {
			e.clear();
		}
	}
	
	public int getEntityCount() {
		return this.entities.size();
	}
	
	public Entity[] getEntityArray() {
		return entities.toArray(new Entity[0]);
	}
}
