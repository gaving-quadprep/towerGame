package gui;

import java.awt.Graphics2D;

public abstract class GUI {
	public int layer;
	public abstract void render(Graphics2D g2);
}
