package entity;

import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;

public class Sheep extends LivingEntity {
	int tileToGoTo;
	public Sheep(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(0, 2, 15, 16);
		this.facing = Direction.LEFT;
		// TODO Auto-generated constructor stub
	}
	public String getSprite() {
		return "sheep.png";
	}
	public void update() {
		super.update();
		this.goRight(true);
	}

	public void render(WorldRenderer wr) {
		if(this.facing == Direction.RIGHT) {
			wr.drawImage(this.sprite, this.x+1, this.y, -1, 1);
		} else {
			wr.drawImage(this.sprite, this.x, this.y, 1, 1);
		}
	}
}
