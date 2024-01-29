package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import entity.Entity;
import main.CollisionChecker;
import towerGame.EventHandler;
import towerGame.Player;

public class Level {
	public int sizeX;
	public int sizeY;
	public int mapTilesForeground[][];
	public int mapTilesBackground[][];
	public BufferedImage tilemap;
	public BufferedImage tilemap_dark;
	public RescaleOp bg_tint;
	public List<Entity> entities=new ArrayList<Entity>();
	private List<Entity> entityQueue=new ArrayList<Entity>();
	public HashMap<String,BufferedImage> sprites = new HashMap<String,BufferedImage>();
	public Player player;
	public float playerStartX;
	public float playerStartY;
	public final ReentrantLock entity_lock = new ReentrantLock();
	public float cameraX;
	public float cameraY;
    public Color skyColor=new Color(98,204,249);
    public boolean inLevelEditor = false;
	
	public Level(int sizeX, int sizeY) {
		mapTilesForeground=new int[sizeX][sizeY];
		mapTilesBackground=new int[sizeX][sizeY];
	    bg_tint = new RescaleOp(0.87f, 0f, null);
		this.sizeX=sizeX;
		this.sizeY=sizeY;
		for(int x=0;x<sizeX;x++) {
			for(int y=0;y<sizeY;y++) {
				mapTilesForeground[x][y]=y>8?5:x==13&y>7&y<9?25:x==13&y<8?10:y==6&x==3|y==8&x==11?26:y==8&x==5|y==7&x==4?7:0;
				mapTilesBackground[x][y]=y>8?8:y>6&y<9&x==7?6:y>2&x>4&x<10?x==6|x==8?y==5?13:y==4?12:3:3:y==2&x>4&x<10&(1&x)==1?3:x==12&y>3?2:0; // temporary
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
						entity.update(eventHandler);
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
		if(!inLevelEditor) {
			if(player.posX<cameraX+3) {
				cameraX=player.posX-3;
			}
			if(player.posX>cameraX+17) {
				cameraX=player.posX-17;
			}
			if(player.posY<cameraY+3) {
				cameraY=player.posY-3;
			}
			if(player.posY>cameraY+12) {
				cameraY=player.posY-12;
			}
		}
	}
	public void update() {
		this.update(null);
	}
	public void render(Graphics2D g2) {
		for(int x=Math.max(0, (int)cameraX);x<Math.min((int)cameraX+21,this.sizeX);x++) {
			for(int y=Math.max(0, (int)cameraY);y<Math.min((int)cameraY+16,this.sizeY);y++) {
				if(mapTilesBackground[x][y]!=0) {
					Tile.tiles[mapTilesBackground[x][y]].render(this,g2,x,y,false);
				}
				if(mapTilesForeground[x][y]!=0) {
					Tile.tiles[mapTilesForeground[x][y]].render(this,g2,x,y,true);
				}
				
			}
		}
		entity_lock.lock();
		try {
			for (Entity entity : this.entities) {
				entity.render(g2);
			}
		} finally {
			entity_lock.unlock();
		}
		if(this.player!=null) {
			this.player.render(g2);
		}
	}
	public void renderBackgroundOnly(Graphics2D g2) {
		for(int x=Math.max(0, (int)cameraX);x<Math.min((int)cameraX+21,this.sizeX);x++) {
			for(int y=Math.max(0, (int)cameraY);y<Math.min((int)cameraY+16,this.sizeY);y++) {
				if(mapTilesBackground[x][y]!=0) {
					Tile.tiles[mapTilesBackground[x][y]].render(this,g2,x,y,false);
				}
				
			}
		}
		entity_lock.lock();
		try {
			for (Entity entity : this.entities) {
				entity.render(g2);
			}
		} finally {
			entity_lock.unlock();
		}
		if(this.player!=null) {
			this.player.render(g2);
		}
	}
	public int getTileForeground(int x,int y) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return 0;
		}
		return mapTilesForeground[x][y];
	}
	public int getTileBackground(int x,int y) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return 0;
		}
		return mapTilesBackground[x][y];
	}
	public void setTileForeground(int x,int y,int tile) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return;
		}
		mapTilesForeground[x][y]=tile;
	}
	public void setTileBackground(int x,int y,int tile) {
		if(x<0|x>=this.sizeX|y<0|y>=this.sizeY){	
			return;
		}
		mapTilesBackground[x][y]=tile;
	}
	public void addEntity(Entity entity) {
		String spriteName = entity.getSprite();
			if(spriteName != "") {
			if(!this.sprites.containsKey(spriteName)) {
				try {
					this.sprites.put(spriteName, ImageIO.read(getClass().getResourceAsStream("/sprites/"+spriteName)));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to load "+spriteName+" sprite", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			entity.setSprite(this.sprites.get(spriteName));
		}
		this.entityQueue.add(entity);
	}
	public void setPlayer(Player player) {
		String spriteName = player.getSprite();
		if(spriteName != "") {
			if(!this.sprites.containsKey(spriteName)) {
				try {
					this.sprites.put(spriteName, ImageIO.read(getClass().getResourceAsStream("/sprites/"+spriteName)));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Failed to load player: "+spriteName+" sprite", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			player.setSprite(this.sprites.get(spriteName));
		}
		this.player=player;
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
					JOptionPane.showMessageDialog(null, "Failed to load player: "+spriteName+" sprite", "Error", JOptionPane.ERROR_MESSAGE);
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
