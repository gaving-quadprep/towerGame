package map;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import entity.Entity;
import main.Main;
import main.WorldRenderer;
import map.interactable.TileData;
import map.interactable.TileWithData;
import towerGame.EventHandler;
import towerGame.Player;

public class Level {
	public int sizeX;
	public int sizeY;
	public int mapTilesForeground[][];
	public int mapTilesBackground[][];
	public TileData[][] tileDataForeground;
	public TileData[][] tileDataBackground;
	public BufferedImage tilemap;
	public BufferedImage tilemap_dark;
	public RescaleOp bg_tint;
	public List<Entity> entities=new ArrayList<Entity>();
	private List<Entity> entityQueue=new ArrayList<Entity>();
	public HashMap<String,BufferedImage> sprites = new HashMap<String,BufferedImage>();
	public Player player;
	public double playerStartX = 4;
	public double playerStartY = 6;
	public final ReentrantLock entity_lock = new ReentrantLock();
	public double cameraX;
	public double cameraY;
	public Color skyColor = new Color(98,204,249);
	public boolean inLevelEditor = false;
	public double gravity;
	public boolean healPlayer = true;
	private Random random = new Random(System.currentTimeMillis());
	public Level(int sizeX, int sizeY) {
		this.mapTilesForeground = new int[sizeX][sizeY];
		this.mapTilesBackground = new int[sizeX][sizeY];
		this.tileDataForeground = new TileData[sizeX][sizeY];
		this.tileDataBackground = new TileData[sizeX][sizeY];
		bg_tint = new RescaleOp(0.87f, 0f, null);
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		for(int x=0;x<sizeX;x++) {
			for(int y=0;y<sizeY;y++) {
				mapTilesForeground[x][y]=y>8?5:x==14&y<9?10:0;
				mapTilesBackground[x][y]=y>8?8:y>6&y<9&x==7?6:y>2&x>4&x<10?x==6|x==8?y==5?13:y==4?12:3:3:y==2&x>4&x<10&(1&x)==1?3:0;
			}
		}
		try {
			tilemap=ImageIO.read(getClass().getResourceAsStream("/sprites/tilemap.png"));
			tilemap_dark=bg_tint.filter(tilemap,null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed to load tilemap", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public Level(int sizeX, int sizeY, boolean inLevelEditor) {
		this(sizeX,sizeY);
		this.inLevelEditor=inLevelEditor;
	}
	public void update(EventHandler eventHandler) {
		if(!inLevelEditor) {
			if(player!=null) {
				if(player.x-3>0)
					if(player.x<cameraX+3)
						cameraX=player.x-3;
				if(player.x-16<sizeX-20)
					if(player.x>cameraX+16)
						cameraX=player.x-16;
				if(player.y<cameraY+3)
					cameraY=player.y-3;
				if(player.y>cameraY+11)
					cameraY=player.y-11;
			}
			for(int x=0;x<this.sizeX;x++) {
				for(int y=0;y<this.sizeY;y++) {
					if(mapTilesBackground[x][y]!=0) {
						Tile.tiles[mapTilesBackground[x][y]].update(this,x,y,false);
					}
					if(mapTilesForeground[x][y]!=0) {
						Tile.tiles[mapTilesForeground[x][y]].update(this,x,y,true);
					}
				}
			}
		}
		entity_lock.lock();
		try {
			if(!inLevelEditor) {
				for (Entity entity : this.entities) {
					if (entity!=null) {
						entity.update();
					}
				}
				if(this.player!=null) {
					this.player.update(eventHandler);
				}
			}
			entities.addAll(entityQueue);
			entityQueue.clear();
			entities.removeIf((Entity e) -> e.markedForRemoval);
		} finally {
			entity_lock.unlock();
		}
		
	}
	public void update() {
		this.update(null);
	}
	public void render(WorldRenderer wr) {
		wr.level = this;
		for(int x = Math.max(0, (int)cameraX);x<Math.min((int)cameraX + Main.width + 2, this.sizeX); x++) {
			for(int y = Math.max(0, (int)cameraY);y<Math.min((int)cameraY + Main.height + 2, this.sizeY); y++) {
				if(mapTilesBackground[x][y]!=0) {
					if(Tile.tiles[mapTilesBackground[x][y]] != null) {
						Tile.tiles[mapTilesBackground[x][y]].render(this,wr,x,y,false);
					}else {
						
					}
				}
				if(mapTilesForeground[x][y]!=0) {
					if(Tile.tiles[mapTilesForeground[x][y]] != null) {
						Tile.tiles[mapTilesForeground[x][y]].render(this,wr,x,y,true);
					}else {
						
					}
				}
				
			}
		}
		entity_lock.lock();
		try {
			for (Entity entity : this.entities) {
				entity.render(wr);
			}
		} finally {
			entity_lock.unlock();
		}
		if(this.player!=null) {
			this.player.render(wr);
		}
	}
	public void renderBackgroundOnly(WorldRenderer wr) {
		for(int x=Math.max(0, (int)cameraX);x<Math.min((int)cameraX + Main.width + 1,this.sizeX);x++) {
			for(int y=Math.max(0, (int)cameraY);y<Math.min((int)cameraY + Main.height + 1,this.sizeY);y++) {
				if(mapTilesBackground[x][y]!=0) {
					Tile.tiles[mapTilesBackground[x][y]].render(this,wr,x,y,false);
				}
			}
		}
	}
	
	public int getTileBackground(int x,int y) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return 0;
		}
		return mapTilesBackground[x][y];
	}
	
	public int getTileForeground(int x,int y) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return 0;
		}
		return mapTilesForeground[x][y];
	}
	
	public int getTile(int x, int y, boolean foreground) {
		if(foreground)
			return getTileForeground(x, y);
		else
			return getTileBackground(x, y);
	}
	
	public TileData getTileDataBackground(int x, int y) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY) {
			return ((TileWithData)Tile.tiles[getTileBackground(x,y)]).defaultTileData.clone();
		}
		if(tileDataBackground[x][y] == null)
			tileDataBackground[x][y] = ((TileWithData)Tile.tiles[getTileBackground(x,y)]).defaultTileData.clone();
		TileData tileData = tileDataBackground[x][y];
		return tileData;
	}
	
	public TileData getTileDataForeground(int x, int y) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return ((TileWithData)Tile.tiles[getTileForeground(x,y)]).defaultTileData.clone();
		}
		
		if(tileDataForeground[x][y] == null)
			tileDataForeground[x][y] = ((TileWithData)Tile.tiles[getTileForeground(x,y)]).defaultTileData.clone();
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
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return;
		}
		mapTilesBackground[x][y]=tile;
		if(Tile.tiles[tile] instanceof TileWithData) {
			TileData td = ((TileWithData)Tile.tiles[tile]).defaultTileData;
			tileDataBackground[x][y] = (td != null ? td.clone() : null);
		}
	}
	
	public void setTileForeground(int x, int y, int tile) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return;
		}
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

	public void setTileDataBackground(int x, int y, TileData td) {
		tileDataBackground[x][y] = td.clone();
	}
	
	public void setTileDataForeground(int x, int y, TileData td) {
		tileDataForeground[x][y] = td.clone();
	}
	
	public void setTileData(int x, int y, TileData td, boolean foreground) {
		if(foreground)
			setTileDataForeground(x, y, td);
		else
			setTileDataBackground(x, y, td);
	}
	
	public void destroy(int x, int y) {
		Tile.tiles[mapTilesForeground[x][y]].onDestroyed(this, x, y);
		setTileForeground(x, y, 0);
	}
	

	
	public void floodFill(int x, int y, int setTile, boolean foreground) {
		Queue<Point> q = new ArrayDeque<Point>();
		q.offer(new Point(x, y));
		int tile = this.getTile(x, y, foreground);
		while(!q.isEmpty()) {
			Point p = q.poll();
			if( !(p.x < 0 || p.x >= this.sizeX || p.y < 0 || p.y >= this.sizeY)) {
				if(tile == setTile) return;
				int t = this.getTile(p.x, p.y, foreground);
				if(t == tile) {
					this.setTile(p.x, p.y, setTile, foreground);
					if(this.getTile(p.x - 1, p.y, foreground) == tile)
						q.offer(new Point(p.x - 1, p.y));
					if(this.getTile(p.x + 1, p.y, foreground) == tile)
						q.offer(new Point(p.x + 1, p.y));
					if(this.getTile(p.x, p.y - 1, foreground) == tile)
						q.offer(new Point(p.x, p.y - 1));
					if(this.getTile(p.x, p.y + 1, foreground) == tile)
						q.offer(new Point(p.x, p.y + 1));
				}
			}
		}
	}
	
	public void addEntity(Entity entity) {
		if (!entity.customSprite) {
			String spriteName = entity.getSprite();
			if(!spriteName.equals("") && spriteName != null) {
				if(!this.sprites.containsKey(spriteName)) {
					try {
						this.sprites.put(spriteName, ImageIO.read(getClass().getResourceAsStream("/sprites/"+spriteName)));
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Failed to load "+spriteName+" sprite", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				entity.setSprite(this.sprites.get(spriteName));
			}
		}
		entity.id = this.random.nextLong();
		this.entityQueue.add(entity);
	}
	
	public void setPlayer(Player player) {
		String spriteName = player.getSprite();
		if(spriteName != "") {
			if(!this.sprites.containsKey(spriteName)) {
				try {
					this.sprites.put(spriteName, ImageIO.read(getClass().getResourceAsStream("/sprites/"+spriteName)));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to load player "+spriteName+" sprite", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			player.setSprite(this.sprites.get(spriteName));
		}
		this.player=player;
		centerCameraOnPlayer();
	}
	public void centerCameraOnPlayer() {
		cameraX = (player.x - 10);
		cameraY = (player.y - 9);

		if(cameraX < 0)
			cameraX = 0;
		if(cameraX > sizeX-20)
			cameraX = sizeX-20;
		if(cameraY < 0)
			cameraY = 0;
		if(cameraY > sizeY-Main.height)
			cameraY = sizeY-Main.height;
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
					JOptionPane.showMessageDialog(null, "Failed to load "+spriteName+" sprite", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			return this.sprites.get(spriteName);
		}
		return null;
		
	}
	public List<Entity> getAllEntities(){
		entity_lock.lock();
		try {
			List<Entity> e2 = new ArrayList<Entity>();
			e2.addAll(this.entities);
			e2.add(player);
			return e2;
		} finally {
			entity_lock.unlock();
		}
	}
}
