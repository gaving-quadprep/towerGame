package entity;

import map.Level;
import util.CollisionChecker;

public class BombGuy extends FollowingEnemy {
	int explodingTime;
	boolean isExploding;
	public BombGuy(Level level) {
		super(level);
		this.attackDamage = 0;
		// TODO Auto-generated constructor stub
	}
	public void update() {
		if(this.isExploding) {
			this.explodingTime--;
			if(this.explodingTime == 0) {
				Explosion explosion = new Explosion(level);
				level.addEntity(explosion);
				explosion.setPosition(x + 0.5, y + 0.5);
				explosion.explode();
			}
		} else {
			//if (CollisionChecker.distance(this, level.player))
		}
	}
	public String getSprite() {
		return "enemy/bombguy.png";
	}
}
