package map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import entity.Entity;
import main.Main;
import map.interactable.*;
import util.CollisionChecker;
import util.Direction;

public class Tile {
	private static int nextId = 0;
	public static int nextCustomTileId = 4096;
	public static int maxTile;
	public final int id;
	public int textureId;
	public final boolean isSolid;
	public final boolean hasCustomHitbox;
	public final Rectangle hitbox;
	public static Tile[] tiles=new Tile[8192];
	public static final Tile[] regularTiles=new Tile[4096];
	public static Tile[] customTiles = new Tile[4096];
	public Tile(int textureId, boolean isSolid) {
		this(nextId++, textureId, isSolid);
	}
	protected Tile(int textureId, boolean isSolid, Rectangle hitbox) {
		this(nextId++, textureId, isSolid, hitbox);
	}
	public Tile(int id, int textureId, boolean isSolid) {
		this.id=id;
		tiles[this.id]=this;
		if(this.id < 4096)
			regularTiles[this.id]=this;
		this.textureId=textureId;	
		this.isSolid=isSolid;
		this.hasCustomHitbox=false;
		this.hitbox=new Rectangle(0,0,16,16);
	}
	public Tile(int id, int textureId, boolean isSolid, Rectangle hitbox) {
		this.id=id;
		tiles[this.id]=this;
		if(this.id < 4096)
			regularTiles[this.id]=this;
		this.textureId=textureId;	
		this.isSolid=isSolid;
		this.hasCustomHitbox=true;
		this.hitbox=hitbox;
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
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {}
	// do not change the order of these
	public static Tile air=new Tile(-1,false);
	public static Tile stone=new Tile(1,true);
	public static Tile crackedStone=new Tile(2,true);
	public static Tile bricks=new Tile(3,true);
	public static Tile crackedBricks=new Tile(4,true);
	public static Tile grass=new Tile(5,true);
	public static Tile wood=new Tile(6,true);
	public static Tile redBricks=new Tile(10,true);
	public static Tile dirt=new Tile(12,true);
	public static Tile log=new Tile(13,true);
	public static Tile boulder=new BoulderTile(11,CollisionChecker.getHitbox(1, 1, 15, 15));
	public static Tile tallGrass=new AnimatedTile(42,false,3);
	public static Tile stoneWindowTop=new Tile(8,true);
	public static Tile stoneWindowBottom=new Tile(9,true);
	public static Tile flower1=new FlowerTile(0);
	public static Tile flower2=new FlowerTile(1);
	public static Tile flower3=new FlowerTile(2);
	public static Tile metalThing=new Tile(14,true,CollisionChecker.getHitbox(4, 4, 12, 12));
	public static Tile stoneVines=new Tile(15,true);
	public static Tile lavaTop=new LavaTile(22,true, CollisionChecker.getHitbox(0, 8, 16, 16));
	public static Tile lavaBottom=new LavaTile(31,false);
	public static Tile cloud=new AnimatedTile(39,false,3);
	public static Tile cloudTransparent = new AnimatedTile(45, false, 3);
	public static Tile spike=new DamageTile(53,true,CollisionChecker.getHitbox(0, 14, 16, 16));
	public static Tile darkBricks=new Tile(55,true);
	public static Tile darkBricksVine=new Tile(56,true);
	public static Tile conveyorLeft=new ConveyorTile(65,true);
	public static Tile conveyorRight=new ConveyorTile(64,true);
	public static Tile bridgeLeft = new Tile(66, true, CollisionChecker.getHitbox(4, 13, 16, 15));
	public static Tile bridge = new Tile(67, true, CollisionChecker.getHitbox(0, 13, 16, 15));
	public static Tile bridgeRight = new Tile(68, true, CollisionChecker.getHitbox(0, 13, 14, 16));
	public static Tile stoneSlab = new Tile(63, true, CollisionChecker.getHitbox(0, 8, 16, 16));
	public static Tile darkStoneTop = new Tile(54, true);
	public static Tile darkStone = new Tile(52, true);
	public static Tile darkStoneCracked = new Tile(77, true);
	public static Tile sign = new InteractableTile(76, false);
	public static Tile hangingSign = new InteractableTile(75, false);
	public static Tile hangingSignLeft = new Tile(74, false);
	public static Tile hangingSignRight = new Tile(73, false);
	public static Tile hangingSignRun = new Tile(72, false);
	public static Tile glass = new Tile(80, true);
	public static Tile checkpoint = new Checkpoint(81, false, CollisionChecker.getHitbox(3, 1, 9 ,16));
	public static Tile exit = new ExitTile(82, false, CollisionChecker.getHitbox(1, 1, 15, 15));
	public static Tile bloodStain = new Tile(48, false);
	public static Tile chest = new ChestTile(84, true, CollisionChecker.getHitbox(0, 2, 16, 16));
	public static Tile tree = new Tile(85, false);
	public static Tile jumpPad = new JumpPadTile(86, false, CollisionChecker.getHitbox(0, 14, 16, 16));
	public static Tile sand = new BoulderTile(78);
	public static Tile skull = new Tile(71, false);
	public static Tile lantern = new Tile(79, false);
	public static Tile fallingTile = new TileThatFallsIfYouStandOnIt(60);
	public static Tile torch = new Tile(7, false);
	public static Tile dirtSeeds = new Tile(57, true);
	public static Tile dirtSeedsVine = new Tile(58, true);
	public static Tile flower4 = new FlowerTile(3);
	public static Tile flower5 = new FlowerTile(4);
	public static Tile flower6 = new FlowerTile(5);
	public static Tile crate = new ChestTile(69, true);
	public static Tile water = new Tile(70, false);
	public static Tile waterWithDirt = new Tile(62, true, CollisionChecker.getHitbox(0, 9, 16, 16));
	public static Tile leaves = new Tile(51, true);
	static {
		maxTile = nextId - 1;
	}
}