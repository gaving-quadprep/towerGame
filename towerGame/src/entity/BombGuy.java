package entity;

import map.Level;
import util.CollisionChecker;

public class BombGuy extends Enemy {
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
		} else {
			//if (CollisionChecker.distance(this, level.player))
		}
	}
	public String getSprite() {
		return "enemy/bombguy.png";
	}
}
