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
		this.attackDamage = 0;
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean canSeePlayer() {
		return !isExploding;
	}
	public void update() {
		super.update();
		if(this.isExploding) {
			this.target = null;
			if (CollisionChecker.distance(this, level.player) > 4)
				this.isExploding = false;
			this.explodingTime--;
			if(this.explodingTime == 0) {
				Explosion explosion = new Explosion(level);
				level.addEntity(explosion);
				explosion.setPosition(x + 0.5, y + 0.5);
				explosion.explode();
				this.isExploding = false;
				this.damageTimer = 0;
				doDamageTo(this, 100);
			}
		} else {
			if (CollisionChecker.distance(this, level.player) < 2) {
				this.isExploding = true;
				this.explodingTime = 60;
			}
		}
	}
	public void render(WorldRenderer wr) {
		boolean shouldBlink = this.isExploding && (this.explodingTime / 5) % 2 == 0;
		if(this.facing == Direction.RIGHT) {
			wr.drawTiledImage(this.sprite, this.x+1, this.y, -1, 1, shouldBlink?16:0, 0, shouldBlink?32:16, 16);
		} else {
			wr.drawTiledImage(this.sprite, this.x, this.y, 1, 1, shouldBlink?16:0, 0, shouldBlink?32:16, 16);
		}
	}
	public String getSprite() {
		return "enemy/bombguy.png";
	}
}
