package map;

import main.Main;

public class AnimatedTile extends Tile {
	int animationFrames;
	public AnimatedTile(int textureId, boolean isSolid, int animationFrames) {
		super(textureId, isSolid);
		this.animationFrames=animationFrames;
	}
	public int getTextureId() {
		return this.textureId+(int)(Main.frames/30)%animationFrames;
	}

}
