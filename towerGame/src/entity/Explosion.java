package entity;

import java.awt.Color;
import java.awt.Rectangle;

import main.Main;
import main.WorldRenderer;
import map.Level;
import util.Position;

public class Explosion extends Entity {
	int size;
	int createdOn;
	Color color = Color.ORANGE;
	Position[] circlePositions;
	double[] circleSizes;
	Color[] circleColors;
	public Explosion(Level level, int size) {
		super(level);
		this.size = size;
		this.createdOn = Main.frames;
		this.circlePositions = new Position[size+2];
		this.circleSizes = new double[size+2];
		this.circleColors = new Color[size+2];
		this.hitbox = new Rectangle(-8, -8, 8, 8);
		for(int i=0; i<size+2; i++) {
			circlePositions[i] = new Position((Math.random() * 2) - 1, (Math.random() * 2) - 1);
			circleSizes[i] = (Math.random() / 1.5) + 0.5;
			circleColors[i] = new Color(Math.min(color.getRed() + (int)(Math.random()*20), 255),Math.min(color.getGreen() + (int)(Math.random()*20), 255),Math.min(color.getBlue() + (int)(Math.random()*20), 255));
		}
		// TODO Auto-generated constructor stub
	}
	public Explosion(Level level) {
		this(level, 5);
	}
	public void render(WorldRenderer wr) {
		for(int i=0; i<size+2; i++) {
			wr.fillEllipse(this.x + circlePositions[i].x, this.x + circlePositions[i].x + this.circleSizes[i], this.y + circlePositions[i].y, this.y + circlePositions[i].y + this.circleSizes[i], circleColors[i]);
		}
	}
}
