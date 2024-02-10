package weapon;

import entity.Entity;
import entity.FireProjectile;
import entity.Thing;
import main.CollisionChecker;
import main.Direction;
import map.Level;
import towerGame.Player;

public class Shield extends Weapon {
	public Shield(int id, String texture) {
		super(id, texture, 0);
	}
	public void onMouseHeld(Level level, Player player, int mouseX, int mouseY) {
		for ( Entity e : level.entities ) {
			if(e instanceof FireProjectile) {
				if(!((FireProjectile)e).hasBeenReflected) {
					if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
						FireProjectile e2=((FireProjectile)e);
						e2.xVelocity=-e2.xVelocity/3;
						e2.yVelocity=-e2.yVelocity/4;
						e2.hasBeenReflected = true;
					}
				}
			}
			if(e instanceof Thing) {
				if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
					Thing e2=((Thing)e);
					e2.xVelocity=-e2.xVelocity/2;
					e2.yVelocity=-e2.yVelocity/4;
				}
			}
		}
	}

}
