package entity;

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
	public void update() {
		super.update();
		if(this.attackCooldown == 0 || target == null) {
			this.isAttacking = false;
			if(canSeePlayer()) {
				this.attackCooldown = 45;
				if(this.canGoTo((int)Math.round(level.player.x), (int)Math.round(level.player.y))) {
					this.target = level.player;
				}else {
					this.attackCooldown = 0;
				}
			}
		}else {
			if(!isAttacking && CollisionChecker.distanceTaxicab(this, target) > 1) {
				if(this.x > target.x) {
					this.goLeft(true);
				}else {
					this.goRight(true);
				}
			}else {
				this.attackCooldown--;
				if(attackCooldown < 0)
					attackCooldown = 0;
				this.isAttacking = true;
			}
		}
	}
}
