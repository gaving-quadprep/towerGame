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
			SerializedData sd = new SerializedData();
			sd.setObject(opened, "opened");   
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			this.opened = (boolean) sd.getObjectDefault("opened", "false");
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
		// TODO Auto-generated constructor stub
	}

}
