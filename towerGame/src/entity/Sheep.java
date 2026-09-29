package entity;

import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;

public class Sheep extends LivingEntity {
	int tileToGoTo;
	int timer;
	public Sheep(Level level) {
		super(level);
		hitbox = CollisionChecker.getHitbox(0, 2, 15, 16);
		facing = Direction.LEFT;
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getSprite() {
		return "sheep.png";
	}
	@Override
	public void update() {
		super.update();

	}

	@Override
	public void render(WorldRenderer wr) {
		if(facing == Direction.RIGHT) {
			wr.drawImage(sprite, x+1, y, -1, 1);
		} else {
			wr.drawImage(sprite, x, y, 1, 1);
		}
	}
}
