package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.Main;
import map.Level;
import save.SerializedData;

public class Decoration extends Entity {
	public int imageSizeX;
	public int imageSizeY;
	public Decoration(Level level) {
		super(level);
		this.customSprite = true;
		this.hitbox = new Rectangle(0,0,0,0);
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite,positions[0],positions[1],Main.scale*imageSizeX,Main.scale*imageSizeY,null);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.imageSizeX, "imageSizeX");
		sd.setObject(this.imageSizeY, "imageSizeY");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		this.imageSizeX = (int)sd.getObjectDefault("imageSizeX",16);
		this.imageSizeY = (int)sd.getObjectDefault("imageSizeY",16);
	}
}
