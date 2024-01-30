package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.Main;
import map.Level;

import java.awt.Rectangle;

public class Entity implements Serializable {
	private static final long serialVersionUID = -4794844253874087884L;
	public transient BufferedImage sprite;
	public boolean customSprite = false;
	public float posX;
	public float posY;
	public long id;
	public Rectangle hitbox;
	public transient Level level;
	public boolean markedForRemoval;
	public Entity(Level level) {
		this.level=level;
	}
	public void update() {}
	public void render(Graphics2D g2) {}
	public void renderDebug(Graphics2D g2) {}
	public String getSprite(){return "";}
	public void setSprite(BufferedImage sprite) {this.sprite=sprite;}
	public void setPosition(float x, float y) {
		this.posX=x;
		this.posY=y;
	}
	public int[] getPositionOnScreen() {
		int[] positions = {(int) (this.posX*Main.tileSize-this.level.cameraX*Main.tileSize),(int) (this.posY*Main.tileSize-this.level.cameraY*Main.tileSize)};
		return positions;
	}
	
}
