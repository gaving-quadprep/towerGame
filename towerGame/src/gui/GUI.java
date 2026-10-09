package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import map.Level;
import towerGame.TowerGame;

public abstract class GUI {
	private List<UIComponent> components;
	public static final FontRenderer fontRenderer = new FontRenderer();
	public static Color backgroundColor = new Color(0, 0, 0, 127);
	public int layer;
	public static final void drawRectHighlightable(Graphics2D g2, int x, int y, int w, int h, Color color, Color highlightColor) {
		Point mousePos = TowerGame.gamePanel.getEventHandler().getMousePos();
		if(mousePos.x < x+w && mousePos.x > x && mousePos.y < y+h && mousePos.y > y) {
			g2.setColor(highlightColor);
		}else {
			g2.setColor(color);
		}
		g2.fillRect(x, y, w, h);
	}
	public void render(Graphics2D g2, Level level) {
		for (UIComponent c : components) {
			c.render(g2);
		}
	}
	public void onMouseClick(Point mousePos) {

	}
	public void onMouseRightClick(Point mousePos) {

	}
	public void onShown() {
		
	}
}
