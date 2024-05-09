package entity;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.WorldRenderer;
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
	public Decoration(Level level, BufferedImage texture) {
		this(level);
		this.sprite = texture;
		this.imageSizeX = texture.getWidth();
		this.imageSizeY = texture.getHeight();
		this.hitbox = new Rectangle(0, imageSizeX, 0, imageSizeY);
	}
	public void render(WorldRenderer wr) {
		wr.drawImage(this.sprite, this.x, this.y, ((double)this.imageSizeX)/16, ((double)this.imageSizeY)/16);
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
