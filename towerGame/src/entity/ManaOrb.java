package entity;

import java.awt.Graphics2D;

import main.CollisionChecker;
import main.Main;
import map.Level;
import towerGame.Player;

public class ManaOrb extends Entity {
	public ManaOrb(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(2,2,13,13);
	}
	public String getSprite() {
		return "manaorb.png";
	}
	public void update() {
		Player p = this.level.player;
		if(p!=null) {
			if(CollisionChecker.checkEntities(this, p)) {
				p.mana=15.0f;
				this.markedForRemoval=true;
			}
		}
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite,positions[0],positions[1],Main.tileSize,Main.tileSize,null);
	}

}
