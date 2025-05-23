package util;

import java.awt.Rectangle;

import entity.Entity;
import map.Level;
import map.Tile;

public abstract class CollisionChecker {
	
	private static class EntityPositions {
		double entityLeftX;
		double entityRightX;
		double entityTopY;
		double entityBottomY;
		
		double entityPosX;
		double entityPosY;
	}
	
	public static EntityPositions getEntityPositions(Entity entity) {
		EntityPositions ep = new EntityPositions();
		
		ep.entityLeftX = entity.x + (entity.hitbox.x / 16d);
		ep.entityRightX = entity.x + (entity.hitbox.x / 16d) + (entity.hitbox.width / 16d);
		ep.entityTopY = entity.y + (entity.hitbox.y / 16d);
		ep.entityBottomY = entity.y + (entity.hitbox.y / 16d) + (entity.hitbox.height / 16d);

		ep.entityPosX = entity.x;
		ep.entityPosY = entity.y;
		
		return ep;
	}
	
	public static EntityPositions getEntityPositionsWithMovement(Entity entity, Direction direction, double movement) {
		EntityPositions ep = getEntityPositions(entity);
		switch(direction) {
		case UP:
			ep.entityTopY -= movement;
			ep.entityBottomY -= movement;
			ep.entityPosY -= movement;
			break;
		case DOWN:
			ep.entityTopY += movement;
			ep.entityBottomY += movement;
			ep.entityPosY += movement;
			break;
		case LEFT:
			ep.entityLeftX -= movement;
			ep.entityRightX -= movement;
			ep.entityPosX -= movement;
			break;
		case RIGHT:
			ep.entityLeftX += movement;
			ep.entityRightX += movement;
			ep.entityPosX += movement;
			break;
		}
		return ep;
	}
	
	public static TilePosition[] getTilePositionsOfDirection(EntityPositions ep, Direction direction) {
		TilePosition tilePos1, tilePos2;
		switch(direction) {
		case UP:
			tilePos1 = new TilePosition((int)ep.entityLeftX, (int)ep.entityTopY);
			tilePos2 = new TilePosition((int)ep.entityRightX, (int)ep.entityTopY);
			break;
		case DOWN:
			tilePos1 = new TilePosition((int)ep.entityLeftX, (int)ep.entityBottomY);
			tilePos2 = new TilePosition((int)ep.entityRightX, (int)ep.entityBottomY);
			break;
		case LEFT:
			tilePos1 = new TilePosition((int)ep.entityLeftX, (int)ep.entityBottomY);
			tilePos2 = new TilePosition((int)ep.entityLeftX, (int)ep.entityTopY);
			break;
		case RIGHT:
			tilePos1 = new TilePosition((int)ep.entityRightX, (int)ep.entityBottomY);
			tilePos2 = new TilePosition((int)ep.entityRightX, (int)ep.entityTopY);
			break;
		default: // only added so java will shut up
			return null;
			
		}
		return new TilePosition[] {tilePos1, tilePos2};
	}
	
	public static boolean checkTile(Level level, Entity entity, Direction direction, double movement) {
		EntityPositions ep = getEntityPositionsWithMovement(entity, direction, movement);
		
		TilePosition[] tilePositions = getTilePositionsOfDirection(ep, direction);
		
		for(int i=0; i<tilePositions.length; i++) {
			int tileNum = level.getTileForeground(tilePositions[i].x, tilePositions[i].y);
			Tile tile = Tile.tiles[tileNum];
			if(tile.isSolid) {
				if(tile.hasCustomHitbox) {
					if((direction == Direction.DOWN || tileNum != Tile.ladder.id) && checkHitboxes(entity.hitbox, tile.hitbox, ep.entityPosX, ep.entityPosY, tilePositions[i].x, tilePositions[i].y)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	public static boolean checkSpecificTiles(Level level, Entity entity, Direction direction, double movement, Tile... tiles) {
		EntityPositions ep = getEntityPositionsWithMovement(entity, direction, movement);
		
		TilePosition[] tilePositions = getTilePositionsOfDirection(ep, direction);
		
		for(int i=0; i<tilePositions.length; i++) {
			int tileNum = level.getTileForeground(tilePositions[i].x, tilePositions[i].y);
			Tile tile = Tile.tiles[tileNum];
			if(containsTile(tiles, tile)) {
				if(tile.hasCustomHitbox) {
					if((direction == Direction.DOWN || tileNum != Tile.ladder.id) && checkHitboxes(entity.hitbox, tile.hitbox, ep.entityPosX, ep.entityPosY, tilePositions[i].x, tilePositions[i].y)) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void checkForTileTouch(Level level, Entity entity, Direction direction, double movement) {
		EntityPositions ep = getEntityPositionsWithMovement(entity, direction, movement);
		
		TilePosition[] tilePositions = getTilePositionsOfDirection(ep, direction);
		
		for(int i=0; i<tilePositions.length; i++) {
			int tileNum = level.getTileForeground(tilePositions[i].x, tilePositions[i].y);
			Tile tile = Tile.tiles[tileNum];
			if(tile.hasCustomHitbox) {
				if((direction == Direction.DOWN || tileNum != Tile.ladder.id) && checkHitboxes(entity.hitbox, tile.hitbox, ep.entityPosX, ep.entityPosY, tilePositions[i].x, tilePositions[i].y)) {
					tile.onTouch(level, entity, direction, tilePositions[i].x, tilePositions[i].y);
				}
			} else {
				tile.onTouch(level, entity, direction, tilePositions[i].x, tilePositions[i].y);
			}
		}
		
	}
	
	
	public static boolean checkSpecificTile(Level level, Entity entity, Direction direction, double movement, Tile tile) {
		return checkSpecificTiles(level, entity, direction, movement, new Tile[] {tile});
	}
		
	public static int[] getTilePositions(Level level, Entity entity, Direction direction, double movement) {
		EntityPositions ep = getEntityPositionsWithMovement(entity, direction, movement);
		int[] positions={(int)ep.entityLeftX,(int)ep.entityRightX,(int)ep.entityTopY,(int)ep.entityBottomY};
		return positions;
	}
	
	public static int[] getTilePositions(Level level, Entity entity) {
		EntityPositions ep = getEntityPositions(entity);
		int[] positions={(int)ep.entityLeftX,(int)ep.entityRightX,(int)ep.entityTopY,(int)ep.entityBottomY};
		return positions;
	}
	
	public static Rectangle getHitbox(int x0, int y0, int x1, int y1) {
		return new Rectangle(x0, y0, x1-x0, y1-y0);
	}
	
	public static boolean checkAABB(double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
		return (x0<=x3)&&(x1>=x2)&&(y0<=y3)&&(y1>=y2);
	}
	
	public static boolean checkHitboxes(Rectangle h1, Rectangle h2, double h1x, double h1y, double h2x, double h2y) {
		return checkAABB(h1x + (h1.x/16d), h1y + (h1.y/16d),
				h1x + (h1.x/16d) + (h1.width/16d), h1y + (h1.y/16d) + (h1.height/16d),
				h2x + (h2.x/16d), h2y + (h2.y/16d),
				h2x + (h2.x/16d) + (h2.width/16d), h2y + (h2.y/16d) + (h2.height/16d));
	}
	
	public static boolean checkEntities(Entity e1, Entity e2) {
		return checkHitboxes(e1.hitbox,e2.hitbox,e1.x,e1.y,e2.x,e2.y);
	}
	
	public static boolean containsTile(Tile[] array, Tile tile) {
		for(int i = 0; i < array.length; i++) {
			if(array[i] == tile)
				return true;
		}
		return false;
	}
	
	public static double distance(Entity e1, Entity e2) {
		return Math.hypot(Math.abs(e1.x-e2.x), Math.abs(e1.y-e2.y));
	}
	
	public static double distanceTaxicab(Entity e1, Entity e2) {
		return Math.abs(e1.x-e2.x) + Math.abs(e1.y-e2.y);
	}
	
	
}
