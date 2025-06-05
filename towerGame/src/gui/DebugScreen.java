package gui;

import java.awt.Graphics2D;

import entity.Entity;
import main.Main;
import map.Level;
import towerGame.TowerGame;
import util.CollisionChecker;
import util.PixelPosition;

public class DebugScreen extends GUI {
	public DebugScreen() {
		this.layer = 24;
	}

	@Override
	public void render(Graphics2D g2, Level level) {
		GUI.fontRenderer.drawText(g2, "Tower Quest version "+Main.version,10,30);
		GUI.fontRenderer.drawText(g2, String.valueOf((int)(1000 / TowerGame.gamePanel.drawTime)) + " FPS",10,30+7*Main.scale);
		GUI.fontRenderer.drawText(g2, String.valueOf(level.getEntityCount())+ " entities",10,30+14*Main.scale);
		GUI.fontRenderer.drawText(g2, "Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,30+21*Main.scale);
		GUI.fontRenderer.drawText(g2, "Thread Count: "+Thread.activeCount(),10,30+28*Main.scale);
		GUI.fontRenderer.drawText(g2, "Java Version: "+System.getProperty("java.version"),10,30+35*Main.scale);
		
		
		if(TowerGame.gamePanel.getEventHandler().showEntityDebug) {
			level.entity_lock.lock();
			level.forEachEntity(false, (e) -> {
				PixelPosition pp = Main.worldRenderer.positionToPixel(e.x, e.y);
				GUI.fontRenderer.drawText(g2, e.getDebugString(), pp.x, pp.y - 12 * Main.scale);
				//CollisionChecker.renderDebug(level,e,g2);
			});
			level.entity_lock.unlock();
		}
	}
}
