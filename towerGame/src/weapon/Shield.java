package weapon;

import entity.Entity;
import entity.PlayerProjectile;
import entity.Projectile;
import entity.Thing;
import map.Level;
import map.sound.SoundManager;
import towerGame.Player;
import util.CollisionChecker;
import util.Direction;

public class Shield extends Weapon {
	public Shield(int id, String texture) {
		super(id, texture, 0);
	}
	public void onMouseHeld(Level level, Player player, int mouseX, int mouseY) {
		for ( Entity e : level.entities ) {
			if(e instanceof Projectile && !(e instanceof PlayerProjectile)) {
				if(!((Projectile)e).hasBeenReflected) {
					if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
						Projectile e2=((Projectile)e);
						e2.xVelocity=-e2.xVelocity/2;
						e2.yVelocity=-e2.yVelocity/4;
						e2.hasBeenReflected = true;
						SoundManager.setFile("shield.wav");
						SoundManager.play();
					}
				}
			}
			if(e instanceof Thing) {
				if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
					Thing e2=((Thing)e);
					e2.xVelocity=-e2.xVelocity/2;
					e2.yVelocity=-e2.yVelocity/4;
					SoundManager.setFile("shield.wav");
					SoundManager.play();
				}
			}
		}
	}

}
