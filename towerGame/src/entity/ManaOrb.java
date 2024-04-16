package entity;

import java.math.BigDecimal;

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
}
