package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import map.Level;
import map.Tile;

public class CollisionChecker {
	public static boolean checkTile(Level level, Entity entity, Direction direction, double movement) {
		double entityLeftX=entity.x
				+((double)entity.hitbox.x/16);
		double entityRightX=entity.x+((double)entity.hitbox.x/16)+((double)entity.hitbox.width/16);
		double entityTopY=entity.y+((double)entity.hitbox.y/16);
		double entityBottomY=entity.y+((double)entity.hitbox.y/16)+((double)entity.hitbox.height/16);
		double entityPosX = entity.x;
		double entityPosY = entity.y;
		int tileNum1,tileNum2;
		switch(direction) {
			case UP:
				entityTopY-=movement;
				entityPosY-=movement;
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityTopY)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityTopY)) {
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
				entityPosY+=movement;
				if(entityRightX<0) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityBottomY)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityBottomY)) {
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
				entityPosX-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(Tile.tiles[tileNum1].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityBottomY)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityTopY)) {
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
				entityPosX+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				
				tileNum1=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(Tile.tiles[tileNum1].isSolid||Tile.tiles[tileNum2].isSolid) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {

						if(Tile.tiles[tileNum1].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityBottomY)) {
							return true;
						}
						if(Tile.tiles[tileNum2].isSolid && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityTopY)) {
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
	public static boolean checkSpecificTiles(Level level, Entity entity, Direction direction, double movement, List<Tile> tiles) {
		double entityLeftX=entity.x
				+((double)entity.hitbox.x/16);
		double entityRightX=entity.x+((double)entity.hitbox.x/16)+((double)entity.hitbox.width/16);
		double entityTopY=entity.y+((double)entity.hitbox.y/16);
		double entityBottomY=entity.y+((double)entity.hitbox.y/16)+((double)entity.hitbox.height/16);
		double entityPosX = entity.x;
		double entityPosY = entity.y;
		int tileNum1,tileNum2;
		switch(direction) {
			case UP:
				entityTopY-=movement;
				entityPosY-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityTopY)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityTopY)) {
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
				entityPosY+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityBottomY)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityBottomY)) {
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
				entityPosX-=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityBottomY)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityTopY)) {
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
				entityPosX+=movement;
				if(((int)entityBottomY>level.sizeY)|((int)entityTopY<0)|((int)entityRightX>level.sizeX)|((int)entityLeftX<0)) {
					return false;
				}
				
				tileNum1=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(tiles.contains(Tile.tiles[tileNum1])||tiles.contains(Tile.tiles[tileNum2])) {
					if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
						if(tiles.contains(Tile.tiles[tileNum1]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityBottomY)) {
							return true;
						}
						if(tiles.contains(Tile.tiles[tileNum2]) && checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityTopY)) {
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
	public static void checkForTileTouch(Level level, Entity entity, Direction direction, double movement) {
		double entityLeftX=entity.x
				+((double)entity.hitbox.x/16);
		double entityRightX=entity.x+((double)entity.hitbox.x/16)+((double)entity.hitbox.width/16);
		double entityTopY=entity.y+((double)entity.hitbox.y/16);
		double entityBottomY=entity.y+((double)entity.hitbox.y/16)+((double)entity.hitbox.height/16);
		double entityPosX = entity.x;
		double entityPosY = entity.y;
		int tileNum1,tileNum2;
		switch(direction) {
			case UP:
				entityTopY-=movement;
				entityPosY-=movement;
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityTopY)) {
						Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityLeftX,(int)entityTopY);
					}
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityTopY)) {
						Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityRightX,(int)entityTopY);
					}
				}else {
					Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityLeftX,(int)entityTopY);
					Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityRightX,(int)entityTopY);
				}
				break;
			case DOWN:
				entityBottomY+=movement;
				entityPosY+=movement;
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityBottomY)) {
						Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityLeftX,(int)entityBottomY);
					}
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityBottomY)) {
						Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityRightX,(int)entityBottomY);
					}
				}else {
					Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityLeftX,(int)entityTopY);
					Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityRightX,(int)entityTopY);
				}
				break;
			case LEFT:
				entityLeftX-=movement;
				entityPosX-=movement;
				tileNum1=level.getTileForeground((int)entityLeftX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityLeftX,(int)entityTopY);
				
				if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityBottomY)) {
						Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityLeftX,(int)entityBottomY);
						}
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityLeftX, (int)entityTopY)) {
						Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityLeftX,(int)entityTopY);
					}
				}else {
					Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityLeftX,(int)entityBottomY);
					Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityLeftX,(int)entityTopY);
				}
				
				break;
			case RIGHT:
				entityRightX+=movement;
				entityPosX+=movement;
				
				tileNum1=level.getTileForeground((int)entityRightX,(int)entityBottomY);
				tileNum2=level.getTileForeground((int)entityRightX,(int)entityTopY);
				if(Tile.tiles[tileNum1].hasCustomHitbox||Tile.tiles[tileNum2].hasCustomHitbox) {
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum1].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityBottomY)) {
						Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityRightX,(int)entityBottomY);
					}
					if(checkHitboxes(entity.hitbox, Tile.tiles[tileNum2].hitbox, entityPosX, entityPosY, (int)entityRightX, (int)entityTopY)) {
						Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityRightX,(int)entityTopY);
					}
				}else {
					Tile.tiles[tileNum1].onTouch(level, entity, direction, (int)entityRightX,(int)entityBottomY);
					Tile.tiles[tileNum2].onTouch(level, entity, direction, (int)entityRightX,(int)entityTopY);
				}
				break;
			}
	}
	public static boolean checkSpecificTile(Level level, Entity entity, Direction direction, double movement, Tile tile) {
		List<Tile> list = new ArrayList<Tile>();
		list.add(tile);
		return checkSpecificTiles(level, entity, direction, movement, list);
	}
		
	public static int[] getTilePositions(Level level, Entity entity, Direction direction, double movement) {
		double entityLeftX=entity.x
				+((double)entity.hitbox.x/16);
		double entityRightX=entity.x+((double)entity.hitbox.x/16)+((double)entity.hitbox.width/16);
		double entityTopY=entity.y+((double)entity.hitbox.y/16);
		double entityBottomY=entity.y+((double)entity.hitbox.y/16)+((double)entity.hitbox.height/16);
		switch(direction) {
		case UP:
			entityTopY=entity.y+((double)entity.hitbox.y/16)-movement;
			break;
		case DOWN:
			entityBottomY=entity.y+((double)entity.hitbox.y/16)+((double)entity.hitbox.height/16)+movement;
			break;
		case LEFT:
			entityLeftX=entity.x+((double)entity.hitbox.x/16)-movement;
			break;
		case RIGHT:
			entityRightX=entity.x+((double)entity.hitbox.x/16)+((double)entity.hitbox.width/16)+movement;
			break;
		}
		int[] positions={(int)entityLeftX,(int)entityRightX,(int)entityTopY,(int)entityBottomY};
		return positions;
	}
	public static void renderDebug(Level level,Entity entity,Graphics2D g2) {
		g2.setColor(new Color(192,0,0,64));
		double entityLeftX=entity.x+((double)entity.hitbox.x/16);
		double entityRightX=entity.x+((double)entity.hitbox.x/16)+((double)entity.hitbox.width/16);
		double entityTopY=entity.y+((double)entity.hitbox.y/16);
		double entityBottomY=entity.y+((double)entity.hitbox.y/16)+((double)entity.hitbox.height/16);
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
	public static boolean checkAABB(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		return (x0<=x3)&&(x1>=x2)&&(y0<=y3)&&(y1>=y2);
	}
	public static boolean checkHitboxes(Rectangle h1, Rectangle h2, double h1x, double h1y, double h2x, double h2y) {
		return checkAABB(h1x+((double)h1.x/16), h1y+((double)h1.y/16), h1x+((double)h1.x/16)+((double)h1.width/16), h1y+((double)h1.y/16)+((double)h1.height/16), h2x+((double)h2.x/16), h2y+((double)h2.y/16), h2x+((double)h2.x/16)+((double)h2.width/16), h2y+((double)h2.y/16)+((double)h2.height/16));
	}
	public static boolean checkEntities(Entity e1, Entity e2) {
		return checkHitboxes(e1.hitbox,e2.hitbox,e1.x,e1.y,e2.x,e2.y);
	}
	
}
