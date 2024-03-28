package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.Main;
import map.Level;
import towerGame.TowerGame;
import util.CollisionChecker;

public class DebugScreen extends GUI {
	public DebugScreen() {
		this.layer = 24;
	}

	@Override
	public void render(Graphics2D g2, Level level) {
		GUI.fontRenderer.drawText(g2, "TowerGame version "+Main.version,10,30);
		GUI.fontRenderer.drawText(g2, "Height "+String.valueOf(level.sizeY-level.player.y),10,42);
		GUI.fontRenderer.drawText(g2, "Frame time "+String.valueOf(TowerGame.gamePanel.drawTime),10,54);
		GUI.fontRenderer.drawText(g2, String.valueOf(level.entities.size())+ " entities",10,66);
		GUI.fontRenderer.drawText(g2, "Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,78);
	}
}
