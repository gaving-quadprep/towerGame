package weapon;

import map.Level;
import towerGame.Player;
import util.CollisionChecker;

public class Pickaxe extends Weapon {

	public Pickaxe(int id, String texture, double damage) {
		super(id, texture, damage);
	}
	public void onAttack(Level level, Player player, boolean isMouseRight, int mouseX, int mouseY) {
		super.onAttack(level, player, isMouseRight, mouseX, mouseY);
		int[] positions = CollisionChecker.getTilePositions(level, player, player.facing, 0.5);
		
		level.destroyIfCracked(positions[0], positions[2], false);
		level.destroyIfCracked(positions[1], positions[2], false);
		level.destroyIfCracked(positions[0], positions[3], false);
		level.destroyIfCracked(positions[1], positions[3], false);
	}

}
