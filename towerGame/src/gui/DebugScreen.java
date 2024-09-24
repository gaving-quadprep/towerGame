package gui;

import java.awt.Graphics2D;

import main.Main;
import map.Level;
import towerGame.TowerGame;

public class DebugScreen extends GUI {
	public DebugScreen() {
		this.layer = 24;
	}

	@Override
	public void render(Graphics2D g2, Level level) {
		GUI.fontRenderer.drawText(g2, "Tower Quest version "+Main.version,10,30);
		GUI.fontRenderer.drawText(g2, "Height "+String.valueOf(level.sizeY-level.player.y),10,30+7*Main.scale);
		GUI.fontRenderer.drawText(g2, "Frame time "+String.valueOf(TowerGame.gamePanel.drawTime),10,30+14*Main.scale);
		GUI.fontRenderer.drawText(g2, String.valueOf(level.entities.size())+ " entities",10,30+21*Main.scale);
		GUI.fontRenderer.drawText(g2, "Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,30+28*Main.scale);
		GUI.fontRenderer.drawText(g2, "Java Version: "+System.getProperty("java.version"),10,30+35*Main.scale);
	}
}
