package item;

import main.ISerializable;
import main.Registry;
import map.Level;
import save.SerializedData;

public class Item implements ISerializable {
	public static final Registry<Item> itemRegistry = new Registry<Item>();
	public void use(Level level) {}
	@Override
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(itemRegistry.getClassName(this.getClass()), "class");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
	}
	static {
		itemRegistry.addMapping(Item.class, "Item");
		itemRegistry.addMapping(ItemWeapon.class, "ItemWeapon");
	}
}
