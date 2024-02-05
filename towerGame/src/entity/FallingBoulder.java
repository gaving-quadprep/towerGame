package entity;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import main.CollisionChecker;
import main.Direction;
import main.Main;
import map.Level;
import map.Tile;
import save.SerializedData;
import towerGame.Player;

public class FallingBoulder extends Entity {
	public double xVelocity;
	public double yVelocity;
	public boolean onGround=false;
	private transient boolean tmp;
	public FallingBoulder(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(1,1,15,15);
	}
	public void update() {
		this.yVelocity+=0.007F;//gravity
		this.tmp=true;
		if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.y+=yVelocity;
			this.onGround=false;
		}else {
			if(CollisionChecker.checkSpecificTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.conveyorLeft)) {
				if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.075F)) {
					this.x-=0.075;
					this.tmp=false;
				}
			}
			if(CollisionChecker.checkSpecificTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.conveyorRight)) {
				if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.075F)) {
					this.x+=0.075;
					this.tmp=false;
				}
			}
			if(this.tmp) {
				if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/3)) {
					this.y+=yVelocity/3;
				}
				if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/7)) {
					this.y+=yVelocity/7;
				}
				
				if(this.yVelocity>0) {
					this.markedForRemoval=true;
					for(Entity e : this.level.entities) {
						if( e instanceof LivingEntity) 
							if(CollisionChecker.checkEntities(this, e)) 
								((LivingEntity) e).damage(5.0F);
					}
					Player p=this.level.player;
					if(CollisionChecker.checkEntities(this, p)) {
						p.damage(5.0F);
					}else {
						if(!Tile.tiles[level.getTileForeground((int)Math.round(this.x), (int)Math.round(this.y+0.1))].isSolid)
							this.level.setTileForeground((int)Math.round(this.x), (int)Math.round(this.y+0.1), Tile.boulder.id);
					}
				}else {
					this.onGround=false;
				}
			}else {
				this.yVelocity = 0;
			}
		}
	}
	@Override
	public void render(Graphics2D g2) {
		int frameX = 11*16;
		int frameY = 0;
		g2.drawImage(level.tilemap, (int)(x*Main.tileSize-(int)(level.cameraX*Main.tileSize)), (int)(y*Main.tileSize-(int)(level.cameraY*Main.tileSize)), (int)(x*Main.tileSize+Main.tileSize-(int)(level.cameraX*Main.tileSize)), (int)(y*Main.tileSize+Main.tileSize-(int)(level.cameraY*Main.tileSize)), frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
		
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.xVelocity, "xVelocity");
		sd.setObject(this.yVelocity, "yVelocity");
		sd.setObject(this.onGround, "onGround");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		this.yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		this.onGround = (boolean)sd.getObjectDefault("onGround",false);
	}
}
