package main;

import java.awt.Graphics2D;
import java.awt.Image;

import map.Level;
import util.PixelPosition;
import util.Position;

public class WorldRenderer {
	private Graphics2D g2;
	public Level level;
	public PixelPosition positionToPixel(double x, double y) {
		return new PixelPosition((int)Math.round(x*Main.tileSize-this.level.cameraX*Main.tileSize), (int)Math.round(y*Main.tileSize-(int)(level.cameraY*Main.tileSize))); 
	}
	public PixelPosition positionToPixel(Position p) {
		return positionToPixel(p.x, p.y); 
	}
	public WorldRenderer(Graphics2D g2) {
		this.g2=g2;
	}
	public WorldRenderer() {}
	public Graphics2D getGraphics() {
		return this.g2;
	}
	public void setGraphics(Graphics2D g2){
		this.g2 = g2;
	}
	public void drawImage(Image im, double x, double y, double w, double h) {
		PixelPosition position = positionToPixel(x, y);
		if((position.x>0 && (position.x+(int)(w*Main.tileSize))<320*Main.scale) && (position.y>0 && (position.y+(int)(h*Main.tileSize))<240*Main.scale))
			g2.drawImage(im, position.x, position.y, (int)(w*Main.tileSize), (int)(h*Main.tileSize), null);
	}
	public void drawTiledImage(Image im, double x, double y, double w, double h, int x2, int y2, int x3, int y3) {
		PixelPosition position = positionToPixel(x, y);
		if((position.x+(w*Main.scale) > 0 && position.x < 320*Main.scale)&&(position.y+(h*Main.scale) > 0 && position.y < 240*Main.scale))
			g2.drawImage(im, position.x, position.y, position.x + (int)(w*Main.tileSize), position.y + (int)(h*Main.tileSize), x2, y2, x3, y3, null);
	}
	
}
