package weapon;

import java.awt.MouseInfo;
import java.awt.Point;

import entity.PlayerProjectile;
import main.Main;
import map.Level;
import towerGame.Player;
import towerGame.TowerGame;

public class Staff extends Weapon {
	int particleSize;
	public Staff(int id, String texture, int particleSize) {
		super(id, texture, particleSize);
		this.particleSize=particleSize;
	}
	public void onAttack(Level level, Player player, boolean isMouseRight, int mouseX, int mouseY) {
		if(isMouseRight) {
			if(player.mana>=1) {
				Point mousePos= MouseInfo.getPointerInfo().getLocation();
				float angle=(float)Math.atan2((mousePos.x-TowerGame.gamePanel.frame.getLocation().x)-Math.round(player.x*Main.tileSize-(int)(level.cameraX*Main.tileSize)+0.5*Main.tileSize), (mousePos.y-TowerGame.gamePanel.frame.getLocation().y)-Math.round(player.y*Main.tileSize-(int)(level.cameraY*Main.tileSize)+0.5*Main.tileSize));
				PlayerProjectile p = new PlayerProjectile(level, player);
				p.xVelocity=(float) Math.sin(angle)/5;
				p.yVelocity=(float) (Math.cos(angle)/5)-0.1F;
				p.size=particleSize+2;
				level.addEntity(p);
				player.mana -= 1F;
				player.mana = Math.round(player.mana *10.0d) / 10.0f;
			}
		}else {
			if(player.mana>=0.1) {
				Point mousePos= MouseInfo.getPointerInfo().getLocation();
				float angle=(float)Math.atan2((mousePos.x-TowerGame.gamePanel.frame.getLocation().x)-Math.round(player.x*Main.tileSize-(int)(level.cameraX*Main.tileSize)+0.5*Main.tileSize), (mousePos.y-TowerGame.gamePanel.frame.getLocation().y)-Math.round(player.y*Main.tileSize-(int)(level.cameraY*Main.tileSize)+0.5*Main.tileSize));
				PlayerProjectile p = new PlayerProjectile(level, player);
				p.xVelocity=(float) Math.sin(angle)/5;
				p.yVelocity=(float) (Math.cos(angle)/5)-0.1F;
				p.size=particleSize;
				level.addEntity(p);
				player.mana -= 0.1F;
				player.mana = Math.round(player.mana *10.0d) / 10.0f;
			}
		}
	}

}
