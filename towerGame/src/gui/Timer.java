package gui;

import java.awt.Graphics2D;

import main.Main;
import map.Level;

public class Timer extends GUI {

	@Override
	public void render(Graphics2D g2, Level level) {
		GUI.fontRenderer.drawTextRight(g2, String.format("%02.0f", Math.floor((float)Main.frames/3600))+":"
				+String.format("%05.2f", ((float)Main.frames)/60%60), Main.screenWidth - 10, 10);
		//g2.drawString(String.format("%02.0f", Math.floor((float)Main.frames/3600))+":"+String.format("%05.2f", ((float)Main.frames)/60%60),10,20);
	}

}
