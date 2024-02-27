package weapon;

import entity.Entity;
import entity.LivingEntity;
import map.Level;
import map.Tile;
import towerGame.Player;
import util.CollisionChecker;
import util.Direction;

public class Pickaxe extends Weapon {

	public Pickaxe(int id, String texture, double damage) {
		super(id, texture, damage);
		// TODO Auto-generated constructor stub
	}
	public void onAttack(Level level, Player player, boolean isMouseRight, int mouseX, int mouseY) {
		for(Entity e : level.entities) {
			if(e instanceof LivingEntity){
				if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
					((LivingEntity) e).damage(this.damage);
				}
			}
		}
		int[] positions = CollisionChecker.getTilePositions(level, player, player.facing, 0.5);
		if(Tile.isCracked(level.getTileForeground(positions[0], positions[2]))) {
			level.setTileForeground(positions[0], positions[2],0);
		}
		if(Tile.isCracked(level.getTileForeground(positions[1], positions[2]))) {
			level.setTileForeground(positions[1], positions[2],0);
		}
		if(Tile.isCracked(level.getTileForeground(positions[0], positions[3]))) {
			level.setTileForeground(positions[0], positions[3],0);
		}
		if(Tile.isCracked(level.getTileForeground(positions[1], positions[3]))) {
			level.setTileForeground(positions[1], positions[3],0);
		}
	}

}
