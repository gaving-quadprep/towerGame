package map.interactable;

import main.Main;
import map.Level;
import map.Tile;
import save.SerializedData;

public class BlockedExit extends TileWithData {
	public static class CustomTileData extends TileData {
		public boolean opened;
		public boolean opening;
		public int openingStage;

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
			opened = (boolean) sd.getObjectDefault("opened", false);
			opening = (boolean) sd.getObjectDefault("opening", false);
			openingStage = (int) sd.getObjectDefault("openingStage", 0);
		}

	}

	@Override
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(level!=null) {
			CustomTileData td = (CustomTileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
			return td.opened ? textureId+14 : td.opening ? textureId+td.openingStage : textureId;
		}
		return textureId;
	}

	@Override
	public void update(Level level, int x, int y, boolean foreground) {
		CustomTileData td = (CustomTileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
		if(td.opened) {
			if(foreground) {
				level.setTileForeground(x, y, Tile.exit.id);
			}else {
				level.setTileBackground(x, y, Tile.exit.id);
			}
		}
		if(td.opening && Main.frames%8 == 0) {
			td.openingStage++;
			if(td.openingStage==14) {
				td.opening = false;
				td.opened = true;
			}
		}
	}

	public BlockedExit(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		defaultTileData = new CustomTileData();
		// TODO Auto-generated constructor stub
	}

}
