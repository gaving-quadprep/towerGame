package entity;

import main.WorldRenderer;
import map.Level;
import map.Tile;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;
import util.Direction;

public class FallingTile extends Entity {
	public double xVelocity;
	public double yVelocity;
	public boolean onGround = false;
	public boolean lands = true;
	public int timeToWaitBeforeFalling = 0;
	public int tile = Tile.boulder.id;
	private transient boolean tmp;
	public FallingTile(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(1,1,15,15);
	}
	public FallingTile(Level level, int tile) {
		this(level);
		this.tile=tile;
	}
	public FallingTile(Level level, int tile, boolean isFromFallingPlatform) {
		this(level, tile);
		if(isFromFallingPlatform) {
			this.lands = false;
			this.canBeStoodOn = true;
			
		}
	}
	public void update() {
		if(this.timeToWaitBeforeFalling == 0) {
			this.yVelocity += 0.007;
			this.tmp=true;
			if(this.lands) {
				if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
					this.y+=yVelocity;
					this.onGround=false;
				}else {
					if(CollisionChecker.checkSpecificTile(this.level, this, Direction.DOWN, this.yVelocity, Tile.conveyorLeft)) {
						this.y = Math.round(this.y) + 0.058; // magic number that works somehow 
						if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.075F)) {
							this.x-=0.075;
							this.tmp=false;
						}
					}
					if(CollisionChecker.checkSpecificTile(this.level, this, Direction.DOWN, this.yVelocity, Tile.conveyorRight)) {
						this.y = Math.round(this.y) + 0.058;
						if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.075F)) {
							this.x+=0.075;
							this.tmp=false;
						}
					}
					if(this.tmp && this.lands) {
						if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/4)) {
							this.y+=yVelocity/4;
						}
						
						if(this.yVelocity>0) {
							this.markedForRemoval=true;
							if(this.tile == Tile.boulder.id) {
								for(Entity e : this.level.entities) {
									if( e instanceof LivingEntity) 
										if(CollisionChecker.checkEntities(this, e)) 
											((LivingEntity) e).damage(5.0F);
								}
							}
							Player p=this.level.player;
							if(CollisionChecker.checkEntities(this, p)) {
								if(this.tile == Tile.boulder.id)
									p.damage(5.0F);
							}else {
								if(!Tile.tiles[level.getTileForeground((int)Math.round(this.x), (int)Math.round(this.y+0.1))].isSolid)
									this.level.setTileForeground((int)Math.round(this.x), (int)Math.round(this.y+0.1), this.tile);
							}
						}else {
							this.onGround=false;
						}
					}else {
						this.yVelocity = 0;
					}
				}
			}else {
				this.y+=yVelocity;
			}
			if(this.y > level.sizeY + 50) {
				this.markedForRemoval = true;
			}
		}else {
			this.timeToWaitBeforeFalling--;
		}
	}
	
	public void render(WorldRenderer wr) {
		int frameX = (Tile.tiles[this.tile].getTextureId() % 16) * 16;
		int frameY = (Tile.tiles[this.tile].getTextureId() / 16) * 16;
		wr.drawTiledImage(level.tilemap, this.x, this.y, 1, 1, frameX, frameY, frameX+16, frameY+16);
		
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.xVelocity, "xVelocity");
		sd.setObject(this.yVelocity, "yVelocity");
		sd.setObject(this.tile, "tileId");
		sd.setObject(this.onGround, "onGround");
		sd.setObject(this.lands, "lands");
		sd.setObject(this.timeToWaitBeforeFalling, "timeToWaitBeforeFalling");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		this.yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		this.tile = (int)sd.getObjectDefault("tileId",Tile.boulder.id);
		this.onGround = (boolean)sd.getObjectDefault("onGround",false);
		this.lands = (boolean)sd.getObjectDefault("lands",true);
		this.timeToWaitBeforeFalling = (int)sd.getObjectDefault("timeToWaitBeforeFalling", 0);
	}
}
