package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Main;
import map.Level;

public class InventoryGUI extends GUI {

	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(new Color(0,0,0,127));
		g2.fillRect(20*Main.scale, 20*Main.scale, 320*Main.scale-40*Main.scale, 240*Main.scale-40*Main.scale);
	}

}
