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
		customSprite = true;
		hitbox = new Rectangle(0, 0, 0, 0);
	}
	public Decoration(Level level, BufferedImage texture) {
		this(level);
		sprite = texture;
		imageSizeX = texture.getWidth();
		imageSizeY = texture.getHeight();
		hitbox = new Rectangle(0, imageSizeX, 0, imageSizeY);
	}
	@Override
	public void render(WorldRenderer wr) {
		wr.drawImage(sprite, x, y, ((double)imageSizeX)/16, ((double)imageSizeY)/16);
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(imageSizeX, "imageSizeX");
		sd.setObject(imageSizeY, "imageSizeY");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		imageSizeX = (int)sd.getObjectDefault("imageSizeX",16);
		imageSizeY = (int)sd.getObjectDefault("imageSizeY",16);
		hitbox = new Rectangle(0, imageSizeX, 0, imageSizeY);
	}
}
