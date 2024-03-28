package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Main;
import map.Level;

public class TileInteractionGUI extends GUI {
	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(40*Main.scale, 192*Main.scale, 320*Main.scale-80*Main.scale, 24*Main.scale);
		g2.setColor(Color.WHITE);
	}

}
