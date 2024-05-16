package map.interactable;

import save.ISerializable;
import save.SerializedData;
import util.Registry;

public abstract class TileData implements ISerializable, Cloneable {
	public static final Registry<TileData> registry = new Registry<TileData>();
	public TileData() {}
	public TileData clone() { 
		try {
			return (TileData) super.clone();
		}catch(Exception e) {
			TileData td = registry.createByName(registry.getClassName(this.getClass()), new Class[] {}, new Object[] {});
			td.deserialize(this.serialize());
			return td;
		}
	}
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(registry.getClassName(getClass()), "class");
		return sd;
	}
	static {
		registry.addMapping(BlockedExit.CustomTileData.class, "BlockedExit");
		registry.addMapping(ChestTile.CustomTileData.class, "ChestTile");
		registry.addMapping(ExtendableSpikes.CustomTileData.class, "ExtendableSpikes");
		registry.addMapping(EntityFactory.CustomTileData.class, "EntityFactory");
	}
}
