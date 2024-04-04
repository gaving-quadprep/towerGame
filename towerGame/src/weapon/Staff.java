package weapon;

import java.awt.Point;
import java.math.BigDecimal;

import entity.PlayerProjectile;
import main.Main;
import map.Level;
import map.sound.SoundManager;
import towerGame.Player;
import towerGame.TowerGame;

public class Staff extends Weapon {
	int projectileSize;
	public Staff(int id, String texture, int projectileSize) {
		super(id, texture, projectileSize);
		this.projectileSize=projectileSize;
	}
	public void onAttack(Level level, Player player, boolean isMouseRight, int mouseX, int mouseY) {
		if(isMouseRight) {
			if(player.mana.compareTo(BigDecimal.ONE)>=0) {
				Point mousePos = TowerGame.gamePanel.getEventHandler().getMousePos();
				double angle=(double)Math.atan2((mousePos.x-TowerGame.gamePanel.frame.getLocation().x)-Math.round(player.x*Main.tileSize-(int)(level.cameraX*Main.tileSize)+0.5*Main.tileSize), (mousePos.y-TowerGame.gamePanel.frame.getLocation().y)-Math.round(player.y*Main.tileSize-(int)(level.cameraY*Main.tileSize)+0.5*Main.tileSize));
				PlayerProjectile p = new PlayerProjectile(level, player);
				p.xVelocity = (double) Math.sin(angle)/5;
				p.yVelocity = (double) (Math.cos(angle)/5)-0.1F;
				p.size=projectileSize+2;
				level.addEntity(p);
				player.mana = player.mana.subtract(BigDecimal.ONE);
				SoundManager.play("shoot.wav");
			}
		}else {
			if(player.mana.compareTo(Main.ONE_TENTH)>=0) {
				Point mousePos = TowerGame.gamePanel.getEventHandler().getMousePos();
				double angle=(double)Math.atan2((mousePos.x-TowerGame.gamePanel.frame.getLocation().x)-Math.round(player.x*Main.tileSize-(int)(level.cameraX*Main.tileSize)+0.5*Main.tileSize), (mousePos.y-TowerGame.gamePanel.frame.getLocation().y)-Math.round(player.y*Main.tileSize-(int)(level.cameraY*Main.tileSize)+0.5*Main.tileSize));
				PlayerProjectile p = new PlayerProjectile(level, player);
				p.xVelocity=(double) Math.sin(angle)/5;
				p.yVelocity=(double) (Math.cos(angle)/5)-0.1F;
				p.size=projectileSize;
				level.addEntity(p);
				player.mana = player.mana.subtract(Main.ONE_TENTH);
				SoundManager.play("shoot.wav");
			}
		}
	}

}
