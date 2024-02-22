package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import main.Main;
import map.Level;

public class FlameDemon extends Enemy {

	public FlameDemon(Level level) {
		super(level);
		this.hitbox = new Rectangle(0, 0, 32, 32);
		// TODO Auto-generated constructor stub
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite, positions[0], positions[1], positions[0]+Main.tileSize*2, positions[1]+Main.tileSize*2, this.isAttacking?16:0, 0, this.isAttacking?32:16, 16, (ImageObserver)null);
	}
	public String getSprite() {
		return "flamedemon.png";
	}

}
