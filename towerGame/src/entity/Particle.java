package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import map.Level;
import save.SerializedData;

public class Particle extends GravityAffectedEntity {
	Color color;
	public Particle(Level level, Color color) {
		super(level);
		this.color=color;
		this.hitbox = new Rectangle(0, 0, 1, 1);
		// TODO Auto-generated constructor stub
	}
	public void render(Graphics2D g2) {
		
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(color, "color");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.color = (Color)sd.getObjectDefault("color", new Color(0, 0, 0));
	}
}
