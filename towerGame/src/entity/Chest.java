package entity;

import map.Level;

public class Chest extends Entity {
	public int item;
	public Chest(Level level) {
		super(level);
		this.hasCollision=true;
	}
	public String getSprite() {
		return "chest.png";
	}

}
