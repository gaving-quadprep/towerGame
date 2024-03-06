package gui;

import java.awt.Graphics2D;

import map.Level;

public abstract class GUI {
	public int layer;
	public abstract void render(Graphics2D g2, Level level);
}
