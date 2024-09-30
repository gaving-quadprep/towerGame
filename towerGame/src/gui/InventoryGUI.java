package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import item.Item;
import main.Main;
import map.Level;
import towerGame.TowerGame;

public class InventoryGUI extends GUI {
	public static final Color slotColor = new Color(192,192,192,127);
	public static final Color slotColor2 = new Color(212,212,212,127);
	public static final void drawRectHighlightable(Graphics2D g2, int x, int y, int w, int h, Color color, Color highlightColor) {
		Point mousePos = TowerGame.gamePanel.getEventHandler().getMousePos();
		if(mousePos.x < x+w && mousePos.x > x && mousePos.y < y+h && mousePos.y > y) {
			g2.setColor(highlightColor);
		}else {
			g2.setColor(color);
		}
		g2.fillRect(x, y, w, h);
	}
	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(20*Main.scale, 20*Main.scale, 320*Main.scale-40*Main.scale, 240*Main.scale-40*Main.scale);
		g2.setColor(slotColor);
		for(int w=0; w<5; w++) {
			for (int h=0; h<3; h++) {
				drawRectHighlightable(g2, 50*Main.scale+45*w*Main.scale, 40*Main.scale+40*h*Main.scale, 32*Main.scale, 32*Main.scale, slotColor, slotColor2);
				Item i = level.player.inventory[h*5+w];
				if(i != null)
					g2.drawImage(i.sprite, 50*Main.scale+45*w*Main.scale, 40*Main.scale+40*h*Main.scale, 32*Main.scale, 32*Main.scale, null);
			}
		}
		g2.drawImage(level.player.sprite, 132*Main.scale, 160*Main.scale, 48*Main.scale, 48*Main.scale, null);
		drawRectHighlightable(g2, 140*Main.scale, 168*Main.scale, 32*Main.scale, 32*Main.scale, slotColor, slotColor2);
	}

}
