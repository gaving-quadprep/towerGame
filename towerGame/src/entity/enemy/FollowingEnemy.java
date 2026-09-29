package entity.enemy;

import entity.Entity;
import map.Level;
import util.CollisionChecker;

public class FollowingEnemy extends Enemy {
	Entity target;
	public FollowingEnemy(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}
	public boolean canSeePlayer() {
		return true;
	}
	@Override
	public void update() {
		super.update();
		if(attackCooldown == 0 || target == null) {
			isAttacking = false;
			if(canSeePlayer()) {
				attackCooldown = 45;
				if(canGoTo((int)Math.round(level.player.x), (int)Math.round(level.player.y))) {
					target = level.player;
				}else {
					attackCooldown = 0;
				}
			}
		} else if(!isAttacking && CollisionChecker.distanceTaxicab(this, target) > 1) {
			if(x > target.x) {
				this.goLeft(true);
			}else {
				this.goRight(true);
			}
		}else {
			attackCooldown--;
			if(attackCooldown < 0)
				attackCooldown = 0;
			isAttacking = true;
		}
	}
}
