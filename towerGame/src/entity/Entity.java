package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Direction;
import main.Main;
import map.Level;
import save.SerializedData;

import java.awt.Rectangle;

public abstract class Entity {
	public BufferedImage sprite;
	public boolean customSprite = false;
	public double x;
	public double y;
	public long id;
	public Rectangle hitbox;
	public transient Level level;
	public boolean markedForRemoval;
	public boolean hasCollision = false;
	public Entity(Level level) {
		this.level=level;
	}
	public void update() {}
	public void render(Graphics2D g2) {}
	public void renderDebug(Graphics2D g2) {}
	public String getSprite(){return "";}
	public void setSprite(BufferedImage sprite) {this.sprite=sprite;}
	public void setPosition(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public final int[] getPositionOnScreen() {
		int[] positions = {(int) (this.x*Main.tileSize-this.level.cameraX*Main.tileSize),(int) (this.y*Main.tileSize-this.level.cameraY*Main.tileSize)};
		return positions;
	}

	public void move(double motion, Direction direction) {
		switch(direction) {
		case UP:
			this.y -= motion;
			break;
		case DOWN:
			this.y += motion;
			break;
		case LEFT:
			this.x -= motion;
			break;
		case RIGHT:
			this.x += motion;
			break;
		}
	}
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(EntityRegistry.getClassName(this.getClass()), "class");
		sd.setObject(this.x, "x");
		sd.setObject(this.y, "y");
		sd.setObject(this.id, "id");
		sd.setObject(this.hitbox, "hitbox");
		sd.setObject(this.customSprite, "customSprite");
		if(this.customSprite) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				ImageIO.write(this.sprite, "png", stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sd.setObject(stream.toByteArray(), "sprite");
		}
		sd.setObject(this.hasCollision, "hasCollision");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		this.x = (double)sd.getObjectDefault("x",0);
		this.y = (double)sd.getObjectDefault("y",0);
		this.id = (long)sd.getObjectDefault("id",-1);
		this.hitbox = (Rectangle)sd.getObjectDefault("hitbox", new Rectangle(0,0,0,0));
		this.customSprite = (boolean)sd.getObjectDefault("customSprite", false);
		if(this.customSprite) {
			ByteArrayInputStream stream = new ByteArrayInputStream((byte[])sd.getObjectDefault("sprite",null));
			if(stream!=null) {
				try {
					this.sprite = ImageIO.read(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.hasCollision = (boolean)sd.getObjectDefault("hasCollision", false);
		
	}
}
