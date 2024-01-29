package entity;

import java.util.List;

import map.Level;

public class NPC extends LivingEntity {
	private static final long serialVersionUID = 1938535554354543364L;
	public boolean killable;
	public List<String> dialog;
	public NPC(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}
	public void damage(float damage) {
		if(this.killable) {
			super.damage(damage);
		}
	}

}
