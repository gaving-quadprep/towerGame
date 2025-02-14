package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import map.Level;
import util.PixelPosition;
import util.Position;

public class WorldRenderer {
	private Graphics2D g2;
	public Level level;
	public PixelPosition positionToPixel(double x, double y) {
		double cameraX = this.level != null ? this.level.cameraX : 0;
		double cameraY = this.level != null ? this.level.cameraY : 0;
		return new PixelPosition((int)Math.round(x*Main.tileSize-cameraX*Main.tileSize), (int)Math.round(y*Main.tileSize-(int)(cameraY*Main.tileSize))); 
	}
	public PixelPosition positionToPixel(Position p) {
		return positionToPixel(p.x, p.y); 
	}
	public WorldRenderer(Graphics2D g2) {
		this.g2 = g2;
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
		boolean shouldRender = true;
		if (level != null)
			shouldRender = (position.x + (Math.abs(w)*Main.tileSize) > 0 && position.x - (Math.abs(w)*Main.tileSize) < 320*Main.scale) && (position.y + (Math.abs(h)*Main.tileSize) > 0 && position.y - (Math.abs(h)*Main.tileSize) < 240*Main.scale);
		if (shouldRender)
			g2.drawImage(im, position.x, position.y, (int)(w*Main.tileSize), (int)(h*Main.tileSize), null);
	}
	public void drawImage(Image im, double x, double y) {
		PixelPosition position = positionToPixel(x, y);
		g2.drawImage(im, position.x, position.y, null);
	}
	
	public void drawRect(double x0, double x1, double y0, double y1, Color color) {
		g2.setColor(color);
		PixelPosition position = positionToPixel(x0, y0);
		PixelPosition position2 = positionToPixel(x1, y1);
		g2.drawRect(position.x, position.y, position2.x-position.x, position2.y-position.y);
	}
	public void fillRect(double x0, double x1, double y0, double y1, Color color) {
		g2.setColor(color);
		PixelPosition position = positionToPixel(x0, y0);
		PixelPosition position2 = positionToPixel(x1, y1);
		g2.fillRect(position.x, position.y, position2.x-position.x, position2.y-position.y);
	}
	
	public void drawEllipse(double x0, double x1, double y0, double y1, Color color) {
		g2.setColor(color);
		PixelPosition position = positionToPixel(x0, y0);
		PixelPosition position2 = positionToPixel(x1, y1);
		g2.drawOval(position.x, position.y, position2.x-position.x, position2.y-position.y);
	}
	public void fillEllipse(double x0, double x1, double y0, double y1, Color color) {
		g2.setColor(color);
		PixelPosition position = positionToPixel(x0, y0);
		PixelPosition position2 = positionToPixel(x1, y1);
		g2.fillOval(position.x, position.y, position2.x-position.x, position2.y-position.y);
	}
	
	public void drawTiledImage(Image im, double x, double y, double w, double h, int x2, int y2, int x3, int y3) {
		PixelPosition position = positionToPixel(x, y);
		boolean shouldRender = true;
		if (level != null)
			shouldRender = (position.x + (Math.abs(w)*Main.tileSize) > 0 && position.x - (Math.abs(w)*Main.tileSize) < 320*Main.scale) && (position.y + (Math.abs(h)*Main.tileSize) > 0 && position.y - (Math.abs(h)*Main.tileSize) < 240*Main.scale);
		if (shouldRender)
			g2.drawImage(im, position.x, position.y, position.x + (int)(w*Main.tileSize), position.y + (int)(h*Main.tileSize), x2, y2, x3, y3, null);
	}
	
}
