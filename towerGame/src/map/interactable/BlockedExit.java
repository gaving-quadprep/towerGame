package map.interactable;

import item.Item;
import map.Level;
import save.SerializedData;

public class BlockedExit extends TileWithData {
	public static class TileData extends BaseTileData{
		boolean opened;
		boolean opening;
		int openingStage;
		
		@Override
		public SerializedData serialize() {
			SerializedData sd = super.serialize();
			sd.setObject(opened, "opened");
			sd.setObject(opening, "opening");
			sd.setObject(openingStage, "openingStage");
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			this.opened = (boolean) sd.getObjectDefault("opened", false);
			this.opening = (boolean) sd.getObjectDefault("opening", false);
			this.openingStage = (int) sd.getObjectDefault("openingStage", 0);
		}
		
	}
	
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(level!=null) {
			TileData td = (TileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
			return td.opened ? this.textureId+14 : td.opening ? this.textureId+td.openingStage : this.textureId;
		}
		return this.textureId;
	}

	public BlockedExit(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		this.defaultTileData = new TileData();
		// TODO Auto-generated constructor stub
	}
	
}
