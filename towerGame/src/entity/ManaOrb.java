package entity;

import java.awt.Graphics2D;
import java.util.List;

import main.CollisionChecker;
import main.EntityRegistry;
import main.Main;
import map.Level;
import towerGame.Player;

public class ManaOrb extends Entity {
	private static final long serialVersionUID = -5431902537063750348L;
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
