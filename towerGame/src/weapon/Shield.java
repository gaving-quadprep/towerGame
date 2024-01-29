package weapon;

import java.awt.MouseInfo;
import java.awt.Point;

import entity.Entity;
import entity.FireProjectile;
import entity.LivingEntity;
import entity.PlayerProjectile;
import entity.Thing;
import main.CollisionChecker;
import main.Direction;
import map.Level;
import towerGame.Player;
import towerGame.TowerGame;

public class Shield extends Weapon {
	public Shield(int id, String texture) {
		super(id, texture, 0);
	}
	public void onMouseHeld(Level level, Player player, int mouseX, int mouseY) {
		for ( Entity e : level.entities ) {
			if(e instanceof FireProjectile) {
				if(!((FireProjectile)e).hasBeenReflected) {
					if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.posX+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.posY,e.posX,e.posY)) {
						FireProjectile e2=((FireProjectile)e);
						e2.xVelocity=-e2.xVelocity/4;
						e2.yVelocity=-e2.yVelocity/4;
						e2.hasBeenReflected = true;
					}
				}
			}
			if(e instanceof Thing) {
				if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.posX+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.posY,e.posX,e.posY)) {
					Thing e2=((Thing)e);
					e2.xVelocity=-e2.xVelocity/4;
					e2.yVelocity=-e2.yVelocity/4;
				}
			}
		}
	}

}
