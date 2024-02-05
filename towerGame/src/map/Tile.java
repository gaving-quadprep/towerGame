package map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import main.CollisionChecker;
import main.Main;

public class Tile {
	public final int id;
	public final int textureId;
	public final boolean isSolid;
	public final boolean hasCustomHitbox;
	public final Rectangle hitbox;
	public static final Tile[] tiles=new Tile[256];
	public static final List<Tile> damage_tiles=new ArrayList<Tile>();
	public Tile(int id,int textureId, boolean isSolid) {
		tiles[id]=this;
		this.id=id;	
		this.textureId=textureId;	
		this.isSolid=isSolid;
		this.hasCustomHitbox=false;
		this.hitbox=new Rectangle(0,0,16,16);
		//this.level=level;
	}
	public Tile(int id,int textureId, boolean isSolid, Rectangle hitbox) {
		tiles[id]=this;
		this.id=id;	
		this.textureId=textureId;	
		this.isSolid=isSolid;
		this.hasCustomHitbox=true;
		this.hitbox=hitbox;
		//this.level=level;
	}
	public int getTextureId() {
		return this.textureId;
	}
	public void update(Level level, int posX, int posY, boolean foreground) {};
	
	public void render(Level level, Graphics2D g2, int posX, int posY, boolean foreground) {
		if(this.id==0) {return;}
		
		int frameX = (this.getTextureId() % 16) * 16;
		int frameY = (this.getTextureId() / 16) * 16;
		if(!foreground) {
			g2.drawImage(level.tilemap_dark, posX*Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize-(int)(level.cameraY*Main.tileSize), posX*Main.tileSize+Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize+Main.tileSize-(int)(level.cameraY*Main.tileSize),frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
		}else {
			g2.drawImage(level.tilemap, posX*Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize-(int)(level.cameraY*Main.tileSize), posX*Main.tileSize+Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize+Main.tileSize-(int)(level.cameraY*Main.tileSize),frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
		}

	}
	public static boolean isCracked(int id) {
		return id == crackedStone.id || id == crackedBricks.id || id == boulder.id || id == darkStoneCracked.id;
	}
	public static Tile air=new Tile(0,-1,false);
	public static Tile stone=new Tile(1,1,true);
	public static Tile crackedStone=new Tile(2,2,true);
	public static Tile bricks=new Tile(3,3,true);
	public static Tile crackedBricks=new Tile(4,4,true);
	public static Tile grass=new Tile(5,5,true);
	public static Tile wood=new Tile(6,6,true);
	public static Tile redBricks=new Tile(7,10,true);
	public static Tile dirt=new Tile(8,12,true);
	public static Tile log=new Tile(9,13,true);
	public static Tile boulder=new BoulderTile(10,11,CollisionChecker.getHitbox(1, 1, 15, 15));
	public static Tile tallGrass=new AnimatedTile(11,42,false,3);
	public static Tile stoneWindowTop=new Tile(12,8,true);
	public static Tile stoneWindowBottom=new Tile(13,9,true);
	public static Tile flower1=new FlowerTile(0);
	public static Tile flower2=new FlowerTile(1);
	public static Tile flower3=new FlowerTile(2);
	public static Tile blackOrb=new Tile(17,14,true,CollisionChecker.getHitbox(4, 4, 12, 12));
	public static Tile stoneVines=new Tile(18,15,true);
	public static Tile lavaTop=new LavaTile(19,22,true, CollisionChecker.getHitbox(0, 8, 16, 16));
	public static Tile lavaBottom=new LavaTile(20,31,false);
	public static Tile cloud=new AnimatedTile(21,39,false,3);
	public static Tile cloudTransparent = new AnimatedTile(22, 45, false, 3);
	public static Tile spike=new DamageTile(23,53,true,CollisionChecker.getHitbox(0, 14, 16, 16));
	public static Tile darkBricks=new Tile(24,55,true);
	public static Tile darkBricksVine=new Tile(25,56,true);
	public static Tile conveyorLeft=new Tile(26,65,true);
	public static Tile conveyorRight=new Tile(27,64,true);
	public static Tile bridgeLeft = new Tile(28, 66, true, CollisionChecker.getHitbox(4, 13, 16, 15));
	public static Tile bridge = new Tile(29, 67, true, CollisionChecker.getHitbox(0, 13, 16, 15));
	public static Tile bridgeRight = new Tile(30, 68, true, CollisionChecker.getHitbox(0, 13, 14, 16));
	public static Tile stoneSlab = new Tile(31, 63, true, CollisionChecker.getHitbox(0, 8, 16, 16));
	public static Tile darkStoneTop = new Tile(32, 54, true);
	public static Tile darkStone = new Tile(33, 52, true);
	public static Tile darkStoneCracked = new Tile(34, 77, true);
	public static Tile hangingSign = new Tile(35, 75, false);
	public static Tile hangingSignLeft = new Tile(36, 74, false);
	public static Tile hangingSignRight = new Tile(37, 73, false);
	public static Tile hangingSignRun = new Tile(38, 72, false);
	public static Tile glass = new Tile(39, 80, true);
	
	
}