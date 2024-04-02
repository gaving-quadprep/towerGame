package map.interactable;

import save.ISerializable;
import save.SerializedData;
import util.Registry;

public abstract class BaseTileData implements ISerializable {
	public static final Registry<BaseTileData> registry = new Registry<BaseTileData>();
	public BaseTileData() {}
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(registry.getClassName(getClass()), "class");
		return sd;
	}
	static {
		registry.addMapping(BlockedExit.TileData.class, "BlockedExit");
		registry.addMapping(ChestTile.TileData.class, "ChestTile");
	}
}
