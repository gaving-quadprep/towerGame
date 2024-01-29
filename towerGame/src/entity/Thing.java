package entity;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.util.List;

import main.Main;
import map.Level;
import towerGame.TowerGame;

public class Thing extends Enemy {
	private static final long serialVersionUID = 3439766573016432470L;
	public boolean isAttacking;
	public Thing(Level level) {
		super(level);
		this.health=5;
		this.maxHealth=5;
		this.attackDamage = 5.0F;
	}
	public String getSprite() {
		return "thing.png";
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize, positions[1]+Main.tileSize, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16, (ImageObserver)null);
	}
}
