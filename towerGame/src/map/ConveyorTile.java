package map;

import entity.Entity;
import entity.GravityAffectedEntity;
import main.CollisionChecker;
import main.Direction;

public class ConveyorTile extends Tile {

	public ConveyorTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof GravityAffectedEntity) {
			GravityAffectedEntity ge = (GravityAffectedEntity)entity;
			if(direction == Direction.DOWN) {
				if(this.id == Tile.conveyorLeft.id) {
					if(!CollisionChecker.checkTile(level, ge, Direction.LEFT, 0.03)) {
						ge.x-=0.03;
					}
					CollisionChecker.checkForTileTouch(level, ge, Direction.LEFT, 0.03);
					ge.xVelocity = -0.03;
				}
				if(this.id == Tile.conveyorRight.id) {
					if(!CollisionChecker.checkTile(level, ge, Direction.LEFT, 0.03)) {
						ge.x+=0.03;
					}
					CollisionChecker.checkForTileTouch(level, ge, Direction.RIGHT, 0.03);
					ge.xVelocity = 0.03;
				}
			}
		}
	}

}
