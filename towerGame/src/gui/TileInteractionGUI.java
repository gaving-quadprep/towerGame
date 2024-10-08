package gui;

import java.awt.Graphics2D;

import main.Main;
import map.Level;

public class TileInteractionGUI extends GUI {
	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(40*Main.scale, 192*Main.scale, 320*Main.scale-80*Main.scale, 24*Main.scale);
		GUI.fontRenderer.drawText(g2, "Press E to interact", 44*Main.scale,202*Main.scale);
	}
}
