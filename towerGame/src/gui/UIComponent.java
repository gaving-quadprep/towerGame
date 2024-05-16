package gui;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class UIComponent {
	public int x;
	public int y;
	public int w;
	public int h;
	public abstract void render(Graphics2D g2);
	public void onClicked(Point pos) {};
	public void onRightClicked(Point pos) {};
}