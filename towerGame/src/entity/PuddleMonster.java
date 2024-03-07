package entity;

import map.Level;

public class PuddleMonster extends Enemy {

	public PuddleMonster(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}
	public void update() {
		super.update();
		this.attackDamage = this.isAttacking ? 1.5 : 0;
	}
	public String getSprite() {
		return "puddle.png";
	}
	public void render() {
		
	}
}
