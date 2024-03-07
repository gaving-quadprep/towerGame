package map.interactable;

import item.Item;
import save.SerializedData;

public class ChestTileData extends TileData {
	
	
	
	public ChestTileData(Item item) {
		this.item = item;
	}
	
	Item item;
	@Override
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(item.serialize(), "item");
		return sd;
	}

	@Override
	public void deserialize(SerializedData sd) {
		this.item = Item.itemRegistry.createByName((String) sd.getObjectDefault("class", "Item"), null, null);
		item.deserialize((SerializedData) sd.getObjectDefault("item", null));
	}

}
