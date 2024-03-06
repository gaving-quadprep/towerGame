package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Main;
import map.Level;

public class PauseMenu extends GUI {

	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(new Color(0,0,0,127));
		g2.fillRect(0,0,320*Main.scale,240*Main.scale);
		g2.setColor(new Color(255, 255, 255, 255));
		
		g2.drawString(String.format("%02.0f", Math.floor((float)Main.frames/3600))+":"+String.format("%05.2f", ((float)Main.frames)/60%60),10,20);
	}

}
