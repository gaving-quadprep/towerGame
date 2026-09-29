package map;

import java.awt.Rectangle;

import entity.Entity;
import entity.enemy.FlameDemon;
import main.Main;
import util.Direction;

public class LavaTile extends DamageTile {
	boolean top;
	public LavaTile(int textureId, boolean top) {
		super(textureId, false);
		this.top=top;
	}
	public LavaTile(int textureId, boolean top, Rectangle hitbox) {
		super(textureId, false, hitbox);
		this.top=top;
	}
	@Override
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(top) {
			return 22+Main.frames/12%8;
		}else {
			return 31+Main.frames/12%8;
		}
	}
	@Override
	public void update(Level level, int x, int y, boolean foreground) {

	}
	@Override
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(!(entity instanceof FlameDemon))
			super.onTouch(level, entity, direction, x, y);
	}
}
