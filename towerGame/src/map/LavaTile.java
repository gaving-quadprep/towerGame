package map;

import java.awt.Rectangle;

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
	public int getTextureId() {
		if(this.top) {
			return 22+(int)(System.nanoTime()/200000000.0D)%8;
		}else {
			return 31+(int)(System.nanoTime()/200000000.0D)%8;
		}
	}
	public void update(Level level, int posX, int posY, boolean foreground) {
		
	}
}
