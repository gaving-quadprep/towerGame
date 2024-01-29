package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import map.Level;
import map.Tile;
import towerGame.TowerGame;

public class CollisionChecker {
	public static boolean checkTile(Level level, Entity entity, Direction direction, float movement) {
		float entityLeftX=entity.posX
				+((float)entity.hitbox.x/16);
		float entityRightX=entity.posX+((float)entity.hitbox.x/16)+((float)entity.hitbox.width/16);
		float entityTopY=entity.posY+((float)entity.hitbox.y/16);
		float entityBottomY=entity.posY+((float)entity.hitbox.y/16)+((float)entity.hitbox.height/16);
		int tileNum1,tileNum2;
		/*float entityLeftCol=entityLeftX/(Main.tileSize);
		float entityRightCol=entityLeftX/(Main.tileSize);
		float entityTopRow=entityLeftX/(Main.tileSize);
		float entityBottomRow=entityLeftX/(Main.tileSize);*/
		switch(direction) {
			case UP:
				entityTopY-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid
								&&(int)entityTopY
								+((float)Tile.tiles[tileNum1].hitbox.y/16)
								+((float)Tile.tiles[tileNum1].hitbox.height/16)
								>entityTopY
								&&((int)entityLeftX
								+((float)Tile.tiles[tileNum1].hitbox.x)/16
								+((float)Tile.tiles[tileNum1].hitbox.width)/16
								>entityLeftX)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid
								&&(int)entityTopY
								+((float)Tile.tiles[tileNum2].hitbox.y/16)
								+((float)Tile.tiles[tileNum2].hitbox.height/16)
								>entityTopY
								&&((int)entityRightX
								+((float)Tile.tiles[tileNum2].hitbox.x)/16
								<entityRightX)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			case DOWN:
				entityBottomY+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid
								&&(int)entityBottomY
								+((float)Tile.tiles[tileNum1].hitbox.y/16)
								<entityBottomY
								&&((int)entityLeftX
								+((float)Tile.tiles[tileNum1].hitbox.x)/16
								+((float)Tile.tiles[tileNum1].hitbox.width)/16
								>entityLeftX)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid
								&&(int)entityBottomY
								+((float)Tile.tiles[tileNum2].hitbox.y/16)
								<entityBottomY
								&&((int)entityRightX
								+((float)Tile.tiles[tileNum2].hitbox.x)/16
								<entityRightX)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			case LEFT:
				entityLeftX-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid
								&&(int)entityLeftX
								+((float)Tile.tiles[tileNum1].hitbox.x/16)
								+((float)Tile.tiles[tileNum1].hitbox.width/16)
								>entityLeftX
								&&((int)entityBottomY
								+((float)Tile.tiles[tileNum1].hitbox.y)/16
								//+((float)Tile.tiles[tileNum1].hitbox.height)/16
								<entityBottomY)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid
								&&(int)entityLeftX
								+((float)Tile.tiles[tileNum2].hitbox.x/16)
								+((float)Tile.tiles[tileNum2].hitbox.width/16)
								>entityLeftX
								&&((int)entityTopY
								+((float)Tile.tiles[tileNum2].hitbox.y)/16
								+((float)Tile.tiles[tileNum2].hitbox.height)/16
								>entityTopY)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			case RIGHT:
				entityRightX+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				
				tileNum1=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid
								&&(int)entityRightX
								+((float)Tile.tiles[tileNum1].hitbox.x/16)
								<entityRightX
								&&((int)entityBottomY
								+((float)Tile.tiles[tileNum1].hitbox.y)/16
								//+((float)Tile.tiles[tileNum1].hitbox.height)/16
								<entityBottomY)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid
								&&(int)entityRightX
								+((float)Tile.tiles[tileNum2].hitbox.x/16)
								<entityRightX
								&&((int)entityTopY
								+((float)Tile.tiles[tileNum2].hitbox.y)/16
								+((float)Tile.tiles[tileNum2].hitbox.height)/16
								>entityTopY)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			}
		return false;
	}
	public static boolean checkSpecificTiles(Level level, Entity entity, Direction direction, float movement, List<Tile> tiles) {
		float entityLeftX=entity.posX
				+((float)entity.hitbox.x/16);
		float entityRightX=entity.posX+((float)entity.hitbox.x/16)+((float)entity.hitbox.width/16);
		float entityTopY=entity.posY+((float)entity.hitbox.y/16);
		float entityBottomY=entity.posY+((float)entity.hitbox.y/16)+((float)entity.hitbox.height/16);
		int tileNum1,tileNum2;
		/*float entityLeftCol=entityLeftX/(Main.tileSize);
		float entityRightCol=entityLeftX/(Main.tileSize);
		float entityTopRow=entityLeftX/(Main.tileSize);
		float entityBottomRow=entityLeftX/(Main.tileSize);*/
		switch(direction) {
			case UP:
				entityTopY-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1])
								&&(int)entityTopY
								+((float)Tile.tiles[tileNum1].hitbox.y/16)
								+((float)Tile.tiles[tileNum1].hitbox.height/16)
								>entityTopY
								&&((int)entityLeftX
								+((float)Tile.tiles[tileNum1].hitbox.x)/16
								+((float)Tile.tiles[tileNum1].hitbox.width)/16
								>entityLeftX)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2])
								&&(int)entityTopY
								+((float)Tile.tiles[tileNum2].hitbox.y/16)
								+((float)Tile.tiles[tileNum2].hitbox.height/16)
								>entityTopY
								&&((int)entityRightX
								+((float)Tile.tiles[tileNum2].hitbox.x)/16
								<entityRightX)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			case DOWN:
				entityBottomY+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1])
								&&(int)entityBottomY
								+((float)Tile.tiles[tileNum1].hitbox.y/16)
								<entityBottomY
								&&((int)entityLeftX
								+((float)Tile.tiles[tileNum1].hitbox.x)/16
								+((float)Tile.tiles[tileNum1].hitbox.width)/16
								>entityLeftX)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2])
								&&(int)entityBottomY
								+((float)Tile.tiles[tileNum2].hitbox.y/16)
								<entityBottomY
								&&((int)entityRightX
								+((float)Tile.tiles[tileNum2].hitbox.x)/16
								<entityRightX)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			case LEFT:
				entityLeftX-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1])
								&&(int)entityLeftX
								+((float)Tile.tiles[tileNum1].hitbox.x/16)
								+((float)Tile.tiles[tileNum1].hitbox.width/16)
								>entityLeftX
								&&((int)entityBottomY
								+((float)Tile.tiles[tileNum1].hitbox.y)/16
								//+((float)Tile.tiles[tileNum1].hitbox.height)/16
								<entityBottomY)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2])
								&&(int)entityLeftX
								+((float)Tile.tiles[tileNum2].hitbox.x/16)
								+((float)Tile.tiles[tileNum2].hitbox.width/16)
								>entityLeftX
								&&((int)entityTopY
								+((float)Tile.tiles[tileNum2].hitbox.y)/16
								+((float)Tile.tiles[tileNum2].hitbox.height)/16
								>entityTopY)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			case RIGHT:
				entityRightX+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				
				tileNum1=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1])
								&&(int)entityRightX
								+((float)Tile.tiles[tileNum1].hitbox.x/16)
								<entityRightX
								&&((int)entityBottomY
								+((float)Tile.tiles[tileNum1].hitbox.y)/16
								//+((float)Tile.tiles[tileNum1].hitbox.height)/16
								<entityBottomY)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2])
								&&(int)entityRightX
								+((float)Tile.tiles[tileNum2].hitbox.x/16)
								<entityRightX
								&&((int)entityTopY
								+((float)Tile.tiles[tileNum2].hitbox.y)/16
								+((float)Tile.tiles[tileNum2].hitbox.height)/16
								>entityTopY)) {
							return true;
						}
						return false;
					}else {
						return true;
					}
				}
				break;
			}
		return false;
	}
	public static boolean checkSpecificTile(Level level, Entity entity, Direction direction, float movement, Tile tile) {
		List<Tile> list = new ArrayList<Tile>();
		list.add(tile);
		return checkSpecificTiles(level, entity, direction, movement, list);
	}
		
	public static int[] getTilePositions(Level level, Entity entity, Direction direction, float movement) {
		float entityLeftX=entity.posX
				+((float)entity.hitbox.x/16);
		float entityRightX=entity.posX+((float)entity.hitbox.x/16)+((float)entity.hitbox.width/16);
		float entityTopY=entity.posY+((float)entity.hitbox.y/16);
		float entityBottomY=entity.posY+((float)entity.hitbox.y/16)+((float)entity.hitbox.height/16);
		switch(direction) {
		case UP:
			entityTopY=entity.posY+((float)entity.hitbox.y/16)-movement;
			break;
		case DOWN:
			entityBottomY=entity.posY+((float)entity.hitbox.y/16)+((float)entity.hitbox.height/16)+movement;
			break;
		case LEFT:
			entityLeftX=entity.posX+((float)entity.hitbox.x/16)-movement;
			break;
		case RIGHT:
			entityRightX=entity.posX+((float)entity.hitbox.x/16)+((float)entity.hitbox.width/16)+movement;
			break;
		}
		int[] positions={(int)entityLeftX,(int)entityRightX,(int)entityTopY,(int)entityBottomY};
		return positions;
	}
	public static void renderDebug(Level level,Entity entity,Graphics2D g2) {
		g2.setColor(new Color(192,0,0,64));
		float entityLeftX=entity.posX+((float)entity.hitbox.x/16);
		float entityRightX=entity.posX+((float)entity.hitbox.x/16)+((float)entity.hitbox.width/16);
		float entityTopY=entity.posY+((float)entity.hitbox.y/16);
		float entityBottomY=entity.posY+((float)entity.hitbox.y/16)+((float)entity.hitbox.height/16);
		g2.fillRect(((int)((int)entityLeftX*Main.tileSize))-(int)(level.cameraX*Main.tileSize),((int)((int)entityBottomY*Main.tileSize))-(int)(level.cameraY*Main.tileSize),Main.tileSize,Main.tileSize);
		if((int)entityLeftX!=(int)entityRightX) {
			g2.fillRect(((int)((int)entityRightX*Main.tileSize))-(int)(level.cameraX*Main.tileSize),((int)((int)entityBottomY*Main.tileSize))-(int)(level.cameraY*Main.tileSize),Main.tileSize,Main.tileSize);
		}
		if((int)entityBottomY!=(int)entityTopY) {
			g2.fillRect(((int)((int)entityLeftX*Main.tileSize))-(int)(level.cameraX*Main.tileSize),((int)((int)entityTopY*Main.tileSize))-(int)(level.cameraY*Main.tileSize),Main.tileSize,Main.tileSize);
		}
		if((int)entityLeftX!=(int)entityRightX&&(int)entityBottomY!=(int)entityTopY) {
			g2.fillRect(((int)((int)entityRightX*Main.tileSize))-(int)(level.cameraX*Main.tileSize),((int)((int)entityTopY*Main.tileSize))-(int)(level.cameraY*Main.tileSize),Main.tileSize,Main.tileSize);
		}
	}
	public static Rectangle getHitbox(int x0,int y0,int x1,int y1) {
		return new Rectangle(x0,y0,x1-x0,y1-y0);
	}
	public static boolean checkAABB(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
		return (x0<=x3)&&(x1>=x2)&&(y0<=y3)&&(y1>=y2);
	}
	public static boolean checkHitboxes(Rectangle h1, Rectangle h2, float h1x, float h1y, float h2x, float h2y) {
		return checkAABB(h1x+((float)h1.x/16), h1y+((float)h1.y/16), h1x+((float)h1.x/16)+((float)h1.width/16), h1y+((float)h1.y/16)+((float)h1.height/16), h2x+((float)h2.x/16), h2y+((float)h2.y/16), h2x+((float)h2.x/16)+((float)h2.width/16), h2y+((float)h2.y/16)+((float)h2.height/16));
	}
	public static boolean checkEntities(Entity e1, Entity e2) {
		return checkHitboxes(e1.hitbox,e2.hitbox,e1.posX,e1.posY,e2.posX,e2.posY);
	}
	
}
