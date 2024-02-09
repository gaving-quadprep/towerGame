package map;

import entity.Entity;
import main.CollisionChecker;
import main.Direction;

public class ConveyorTile extends Tile {

	public ConveyorTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(direction == Direction.DOWN) {
			if(this.id == Tile.conveyorLeft.id) {
				if(!CollisionChecker.checkTile(level, entity, Direction.LEFT, 0.075F)) {
					entity.x-=0.075;
				}
				CollisionChecker.checkForTileTouch(level, entity, Direction.LEFT, 0.075F);
			}
			if(this.id == Tile.conveyorRight.id) {
				if(!CollisionChecker.checkTile(level, entity, Direction.LEFT, 0.075F)) {
					entity.x+=0.075;
				}
				CollisionChecker.checkForTileTouch(level, entity, Direction.RIGHT, 0.075F);
			}
		}
	}

}
