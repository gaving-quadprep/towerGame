package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import map.Level;

public abstract class GUI {
	private List<UIComponent> components;
	public static final FontRenderer fontRenderer = new FontRenderer();
	public static Color backgroundColor = new Color(0, 0, 0, 127);
	public int layer;
	public void render(Graphics2D g2, Level level) {
		for (UIComponent c : components) {
			c.render(g2);
		}
	}
	public void onMouseClick(Point mousePos) {
		
	}
	public void onMouseRightClick(Point mousePos) {
		
	}
}
