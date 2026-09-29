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
		hitbox = CollisionChecker.getHitbox(1, 1, 15, 15);
	}
	public FallingTile(Level level, int tile) {
		this(level);
		this.tile = tile;
	}
	@Override
	public void update() {
		if(timeToWaitBeforeFalling == 0) {
			canLand = true;
			if(lands) {
				super.update();
			} else {
				yVelocity += level.gravity;
				y += yVelocity;

				// auto remove because it doesn't call super.update
				if (y > level.sizeY + 50)
					markedForRemoval = true;
			}
			xVelocity /= 1.5;
		} else {
			timeToWaitBeforeFalling--;
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
				onGround = false;
			} else if (leftTile == 0 && rightTile == 0) {
				// do the thing (i forgot what)
			} else {
				markedForRemoval=true;
				if(tile == Tile.boulder.id) {
					level.forEachEntityOfType(LivingEntity.class, false, (e) -> {
						if(CollisionChecker.checkEntities(this, e)) 
							doDamageTo(e, 5.0F);
					});
				}
				Player p = level.player;
				if(CollisionChecker.checkEntities(this, p)) {
					if(tile == Tile.boulder.id)
						doDamageTo(p, 5.0);
				} else if(!Tile.tiles[level.getTileForeground((int)Math.round(x), (int)Math.round(y + 0.1))].isSolid)
					level.setTileForeground((int)Math.round(x), (int)Math.round(y + 0.1), tile);
			}
		}
	}

	@Override
	public void render(WorldRenderer wr) {
		int frameX = (Tile.tiles[tile].getTextureId() % 16) * 16;
		int frameY = (Tile.tiles[tile].getTextureId() / 16) * 16;
		wr.drawTiledImage(level.tilemap, x, y, 1, 1, frameX, frameY, frameX + 16, frameY + 16);

	}
	@Override
	public String getDebugString() {
		return timeToWaitBeforeFalling > 0 ? "NOT FALLING" : "canLand: " + canLand;
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(tile, "tileId");
		sd.setObject(lands, "lands");
		sd.setObject(timeToWaitBeforeFalling, "timeToWaitBeforeFalling");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		tile = (int)sd.getObjectDefault("tileId",Tile.boulder.id);
		lands = (boolean)sd.getObjectDefault("lands",true);
		timeToWaitBeforeFalling = (int)sd.getObjectDefault("timeToWaitBeforeFalling", 0);
	}
}
