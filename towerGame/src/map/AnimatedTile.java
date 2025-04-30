package map;

import main.Main;

public class AnimatedTile extends Tile {
	public int animationFrames;
	public boolean reversible;
	public AnimatedTile(int textureId, boolean isSolid, int animationFrames, boolean reversible) {
		super(textureId, isSolid);
		this.animationFrames = animationFrames;
		this.reversible = reversible;
	}
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(this.reversible) {
			int frame = Main.frames / 30 % (animationFrames * 2 - 2);
			if (frame >= animationFrames) {
				return (this.textureId + animationFrames + 1) - frame;
			} else {
				return this.textureId + frame;
			}
		} else {
			return this.textureId + (int)(Main.frames/30) % animationFrames;
		}
	}

}
