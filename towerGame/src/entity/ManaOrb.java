package entity;

import java.awt.Graphics2D;
import java.math.BigDecimal;

import main.Main;
import map.Level;
import towerGame.Player;
import util.CollisionChecker;

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
				p.mana=BigDecimal.valueOf(15);
				this.markedForRemoval=true;
			}
		}
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if((positions[0]+(16*Main.scale) > 0 && positions[0] < 320*Main.scale)&&(positions[1]+(16*Main.scale) > 0 && positions[1] < 240*Main.scale))
			g2.drawImage(this.sprite,positions[0],positions[1],Main.tileSize,Main.tileSize,null);
	}

}
