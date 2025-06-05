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
		this.canBeStoodOn = true;
		// TODO Auto-generated constructor stub
	}
	
	public void update() {
		if (timeToWaitBeforeFalling <= 0) {
			this.yVelocity += level.gravity;
			this.y += yVelocity;
		} else {
			timeToWaitBeforeFalling--;
		}
		
		// auto remove because it doesn't call super.update
		if (this.y > level.sizeY + 50)
			this.markedForRemoval = true;
	}
	
	public void render(WorldRenderer wr) {
		super.render(wr);
		int frameX = (textureId % 16) * 16;
		int frameY = (textureId / 16) * 16;
		wr.drawTiledImage(level.tilemap, this.x, this.y, 1, 1, frameX, frameY, frameX + 16, frameY + 16);
	}
	
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.textureId, "textureId");
		sd.setObject(this.timeToWaitBeforeFalling, "timeToWaitBeforeFalling");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.textureId = (int)sd.getObjectDefault("textureId",Tile.fallingTile.getTextureId());
		this.timeToWaitBeforeFalling = (int)sd.getObjectDefault("timeToWaitBeforeFalling", 0);
	}

}
