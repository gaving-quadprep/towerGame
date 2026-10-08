package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Main;
import map.Level;

public class PauseMenu extends Timer {

	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(0,0,320*Main.scale,240*Main.scale);
		g2.setColor(Color.WHITE);

		super.render(g2, level);
	}

}
