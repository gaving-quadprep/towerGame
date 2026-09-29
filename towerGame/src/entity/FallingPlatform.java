package entity;

import main.WorldRenderer;
import map.Level;
import map.Tile;
import save.SerializedData;

// this is just fallingtile
public class FallingPlatform extends PlatformEntity {
	int textureId;
	public int timeToWaitBeforeFalling;

	public FallingPlatform(Level level, int textureId) {
		super(level);
		this.textureId = textureId;
		canBeStoodOn = true;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		if (timeToWaitBeforeFalling <= 0) {
			yVelocity += level.gravity;
			y += yVelocity;
		} else {
			timeToWaitBeforeFalling--;
		}

		// auto remove because it doesn't call super.update
		if (y > level.sizeY + 50)
			markedForRemoval = true;
	}

	@Override
	public void render(WorldRenderer wr) {
		super.render(wr);
		int frameX = (textureId % 16) * 16;
		int frameY = (textureId / 16) * 16;
		wr.drawTiledImage(level.tilemap, x, y, 1, 1, frameX, frameY, frameX + 16, frameY + 16);
	}

	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(textureId, "textureId");
		sd.setObject(timeToWaitBeforeFalling, "timeToWaitBeforeFalling");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		textureId = (int)sd.getObjectDefault("textureId",Tile.fallingTile.getTextureId());
		timeToWaitBeforeFalling = (int)sd.getObjectDefault("timeToWaitBeforeFalling", 0);
	}

}
