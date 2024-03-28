package item;

import java.awt.image.BufferedImage;

import map.Level;
import save.ISerializable;
import save.SerializedData;
import util.Registry;

public class Item implements ISerializable {
	public static final Registry<Item> itemRegistry = new Registry<Item>();
	public BufferedImage sprite;
	public boolean customSprite = false;
	public void use(Level level) {}
	public String getSprite() {
		return "win.png"; //placeholder
	}
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
