package map;

import java.awt.Rectangle;

import entity.Entity;
import entity.FlameDemon;
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
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(this.top) {
			return 22+(int)(Main.frames/12)%8;
		}else {
			return 31+(int)(Main.frames/12)%8;
		}
	}
	public void update(Level level, int x, int y, boolean foreground) {
		
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		if(!(entity instanceof FlameDemon))
			super.onTouch(level, entity, direction, x, y);
	}
}
