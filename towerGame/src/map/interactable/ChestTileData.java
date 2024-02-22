package map.interactable;

import item.Item;
import save.SerializedData;

public class ChestTileData extends TileData {
	Item item;
	@Override
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(item.serialize(), "item");
		return sd;
	}

	@Override
	public void deserialize(SerializedData sd) {

	}

}
