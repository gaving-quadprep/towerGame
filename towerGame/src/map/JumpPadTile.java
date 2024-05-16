package map;

import java.awt.Rectangle;

import entity.Entity;
import entity.LivingEntity;
import towerGame.Player;
import util.Direction;

public class JumpPadTile extends Tile {

	public JumpPadTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}

	public JumpPadTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(entity instanceof LivingEntity && !(entity instanceof  Player) ) {
			if(((LivingEntity) entity).onGround) {
				((LivingEntity) entity).yVelocity = -0.18;
			}
		}
	}
	

}
