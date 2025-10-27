package entity;

import main.WorldRenderer;
import map.Level;
import map.Tile;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;
import util.Direction;

public class FallingTile extends GravityAffectedEntity {
	public boolean lands = true;
	public int timeToWaitBeforeFalling = 0;
	public int tile = Tile.boulder.id;
	
	// tmp
	private transient boolean canLand;
	public FallingTile(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(1, 1, 15, 15);
	}
	public FallingTile(Level level, int tile) {
		this(level);
		this.tile = tile;
	}
	public void update() {
		if(this.timeToWaitBeforeFalling == 0) {
			this.canLand = true;
			if(this.lands) {
				super.update();
			} else {
				this.yVelocity += level.gravity;
				this.y += yVelocity;
				
				// auto remove because it doesn't call super.update
				if (this.y > level.sizeY + 50)
					this.markedForRemoval = true;
			}
			this.xVelocity /= 1.5;
		} else {
			this.timeToWaitBeforeFalling--;
		}
	}
	
	@Override
	public void onHit(Direction direction) {
		if (direction == Direction.DOWN) {
			int[] positions = CollisionChecker.getTilePositions(level, this, direction, yVelocity);
			int leftTile = level.getTileForeground(positions[0], positions[3]);
			int rightTile = level.getTileForeground(positions[1], positions[3]);
			if (leftTile == Tile.conveyorLeft.id || 
					leftTile == Tile.conveyorRight.id || 
					rightTile == Tile.conveyorLeft.id || 
					rightTile == Tile.conveyorRight.id) {
				this.onGround = false;
			} else if (leftTile == 0 && rightTile == 0) {
				// do the thing (i forgot what)
			} else {
				this.markedForRemoval=true;
				if(this.tile == Tile.boulder.id) {
					this.level.forEachEntityOfType(LivingEntity.class, false, (e) -> {
						if(CollisionChecker.checkEntities(this, e)) 
							doDamageTo(e, 5.0F);
					});
				}
				Player p = this.level.player;
				if(CollisionChecker.checkEntities(this, p)) {
					if(this.tile == Tile.boulder.id)
						doDamageTo(p, 5.0);
				} else {
					if(!Tile.tiles[level.getTileForeground((int)Math.round(this.x), (int)Math.round(this.y + 0.1))].isSolid)
						this.level.setTileForeground((int)Math.round(this.x), (int)Math.round(this.y + 0.1), this.tile);
				}
			}
		}
	}
	
	public void render(WorldRenderer wr) {
		int frameX = (Tile.tiles[this.tile].getTextureId() % 16) * 16;
		int frameY = (Tile.tiles[this.tile].getTextureId() / 16) * 16;
		wr.drawTiledImage(level.tilemap, this.x, this.y, 1, 1, frameX, frameY, frameX + 16, frameY + 16);
		
	}
	public String getDebugString() {
		return this.timeToWaitBeforeFalling > 0 ? "NOT FALLING" : "canLand: " + this.canLand;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.tile, "tileId");
		sd.setObject(this.lands, "lands");
		sd.setObject(this.timeToWaitBeforeFalling, "timeToWaitBeforeFalling");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.tile = (int)sd.getObjectDefault("tileId",Tile.boulder.id);
		this.lands = (boolean)sd.getObjectDefault("lands",true);
		this.timeToWaitBeforeFalling = (int)sd.getObjectDefault("timeToWaitBeforeFalling", 0);
	}
}
