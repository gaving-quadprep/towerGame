package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Main;
import map.Level;
import save.SerializedData;

public class Decoration extends Entity implements Cloneable { 
	public int imageSizeX;
	public int imageSizeY;
    public Object clone() { 
        try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
    }
	public Decoration(Level level) {
		super(level);
		this.customSprite = true;
		this.hitbox = new Rectangle(0,0,0,0);
	}
	public Decoration(Level level, BufferedImage texture) {
		this(level);
		this.sprite = texture;
		this.imageSizeX = texture.getWidth();
		this.imageSizeY = texture.getHeight();
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if((positions[0]+(this.imageSizeX*Main.scale) > 0 && positions[0] < 320*Main.scale)&&(positions[1]+(this.imageSizeY*Main.scale) > 0 && positions[1] < 240*Main.scale))
			g2.drawImage(this.sprite,positions[0],positions[1],Main.scale*imageSizeX,Main.scale*imageSizeY,null);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.imageSizeX, "imageSizeX");
		sd.setObject(this.imageSizeY, "imageSizeY");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.imageSizeX = (int)sd.getObjectDefault("imageSizeX",16);
		this.imageSizeY = (int)sd.getObjectDefault("imageSizeY",16);
	}
}
