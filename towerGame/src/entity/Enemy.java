package entity;

import main.CollisionChecker;
import map.Level;

public class Enemy extends LivingEntity{
	private static final long serialVersionUID = -1493297038213441410L;
	public float attackDamage;
	public int attackCooldown;
	public Enemy(Level level) {
		super(level);
		this.damageCooldown = 4;
		this.attackDamage = 1.0F;
	}
	public void update() {
		super.update();
		if(this.level.player!=null) {
			if(CollisionChecker.checkEntities(this,this.level.player)) {
				this.level.player.damage(this.attackDamage);
			}
		}
	}

}
