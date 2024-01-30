package entity;

import map.Level;

public class Chest extends Entity {
	private static final long serialVersionUID = -2970285025432280530L;
	public int item;
	public Chest(Level level) {
		super(level);
		this.hasCollision=true;
	}
	public String getSprite() {
		return "chest.png";
	}

}
