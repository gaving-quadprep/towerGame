package map;

import entity.Entity;
import entity.GravityAffectedEntity;
import util.CollisionChecker;
import util.Direction;

public class ConveyorTile extends Tile {
	private Direction direction;
	public ConveyorTile(int textureId, boolean isSolid, Direction direction) {
		super(textureId, isSolid);
		this.direction = direction;
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof GravityAffectedEntity) {
			GravityAffectedEntity ge = (GravityAffectedEntity)entity;
			if(direction == Direction.DOWN) {
				if(!CollisionChecker.checkTile(level, ge, Direction.LEFT, 0.03)) {
					ge.x += (0.03 * this.direction.toNumber());
				}
				CollisionChecker.checkForTileTouch(level, ge, Direction.LEFT, 0.03);
				ge.xVelocity = (0.033 * this.direction.toNumber());
			}
		}
	}

}
