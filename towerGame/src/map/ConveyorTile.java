package map;

import entity.Entity;
import entity.GravityAffectedEntity;
import util.Direction;

public class ConveyorTile extends Tile {
	private Direction direction;
	public ConveyorTile(int textureId, boolean isSolid, Direction direction) {
		super(textureId, isSolid);
		this.direction = direction;
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof GravityAffectedEntity) {
			GravityAffectedEntity ge = (GravityAffectedEntity)entity;
			if(direction == Direction.DOWN) {
				/*if(!CollisionChecker.checkTile(level, ge, Direction.LEFT, 0.03)) {
					ge.x += (0.025 * this.direction.toNumber());
				}
				CollisionChecker.checkForTileTouch(level, ge, Direction.LEFT, 0.03);*/
				double moveVelocity = (0.09375 * this.direction.toNumber());
				ge.xVelocity = (ge.xVelocity + moveVelocity) / 2;
			}
		}
	}

}
