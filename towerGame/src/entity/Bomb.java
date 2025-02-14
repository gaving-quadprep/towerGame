package entity;

import main.Main;
import map.Level;
import util.CollisionChecker;

public class Bomb extends LivingEntity {
	private boolean goingToExplode = false;
	public Bomb(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(1, 2, 15, 16);
		health = maxHealth = Main.ONE_TENTH;
		this.shouldRenderHealthBar = false;
		// TODO Auto-generated constructor stub
	}
	
	public String getSprite() {
		return "bomb.png";
	}
	
	public void onDied() {
		if(!goingToExplode) {
			goingToExplode = true;
			Explosion explosion = new Explosion(level);
			level.addEntity(explosion);
			explosion.setPosition(x + 0.5, y + 0.5);
			explosion.explode();
		}
	}

}
