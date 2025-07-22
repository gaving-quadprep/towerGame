package weapon;

import entity.Projectile;
import entity.enemy.Thing;
import map.Level;
import sound.SoundManager;
import towerGame.Player;
import util.CollisionChecker;
import util.Direction;

public class Shield extends Weapon {
	public Shield(int id, String texture) {
		super(id, texture, 0);
	}
	public void onMouseHeld(Level level, Player player, int mouseX, int mouseY) {
		level.forEachEntityOfType(Projectile.class, false, (e) -> {
			if(!e.hasBeenReflected) {
				if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
					e.xVelocity *= -0.5;
					e.yVelocity *= -0.25;
					e.hasBeenReflected = true;
					SoundManager.play("shield.wav", 0);
				}
			}
		});
		level.forEachEntityOfType(Thing.class, false, (e) -> {
			if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
				e.xVelocity *= -0.5;
				e.yVelocity *= -0.25;
				SoundManager.play("shield.wav", 0);
			}
		});
	}
}
