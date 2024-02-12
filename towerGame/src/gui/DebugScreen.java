package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import main.CollisionChecker;
import map.Level;
import towerGame.TowerGame;

public class DebugScreen extends GUI {
	public DebugScreen() {
		this.layer = 24;
	}

	@Override
	public void render(Graphics2D g2) {
		Level level = TowerGame.gamePanel.level;
		level.entity_lock.lock();
		for(Entity e : level.entities) {
			CollisionChecker.renderDebug(level,e,g2);
		}
		level.entity_lock.unlock();
		CollisionChecker.renderDebug(level,level.player,g2);
		g2.setColor(new Color(128,0,0,192));
		g2.drawString("TowerGame version 0.2",10,30);
		g2.drawString("Height "+String.valueOf(level.sizeY-level.player.y),10,40);
		g2.drawString("Frame time "+String.valueOf(TowerGame.gamePanel.drawTime),10,50);
		g2.drawString(String.valueOf(level.entities.size())+ " entities",10,60);
		g2.drawString("Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,70);
	}
}
