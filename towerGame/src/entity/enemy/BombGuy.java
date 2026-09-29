package entity.enemy;

import entity.Explosion;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;

public class BombGuy extends FollowingEnemy {
	int explodingTime;
	boolean isExploding;
	public BombGuy(Level level) {
		super(level);
		attackDamage = 0;
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean canSeePlayer() {
		return !isExploding;
	}
	@Override
	public void update() {
		super.update();
		if(isExploding) {
			target = null;
			if (CollisionChecker.distance(this, level.player) > 4)
				isExploding = false;
			explodingTime--;
			if(explodingTime == 0) {
				Explosion explosion = new Explosion(level);
				level.addEntity(explosion);
				explosion.setPosition(x + 0.5, y + 0.5);
				explosion.explode();
				isExploding = false;
				damageTimer = 0;
				doDamageTo(this, 100);
			}
		} else if (CollisionChecker.distance(this, level.player) < 2) {
			isExploding = true;
			explodingTime = 60;
		}
	}
	@Override
	public void render(WorldRenderer wr) {
		boolean shouldBlink = isExploding && (explodingTime / 5) % 2 == 0;
		if(facing == Direction.RIGHT) {
			wr.drawTiledImage(sprite, x+1, y, -1, 1, shouldBlink?16:0, 0, shouldBlink?32:16, 16);
		} else {
			wr.drawTiledImage(sprite, x, y, 1, 1, shouldBlink?16:0, 0, shouldBlink?32:16, 16);
		}
	}
	@Override
	public String getSprite() {
		return "enemy/bombguy.png";
	}
}
