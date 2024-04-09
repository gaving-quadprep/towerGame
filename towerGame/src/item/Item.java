package item;

import java.awt.image.BufferedImage;

import entity.Entity;
import map.Level;
import save.ISerializable;
import save.SerializedData;
import util.Registry;

public class Item implements ISerializable, Cloneable {
	public static final Registry<Item> itemRegistry = new Registry<Item>();
	public BufferedImage sprite;
	public boolean customSprite = false;
	public void use(Level level) {}
	public String getSprite() {
		return "item/shield.png"; //placeholder
	}
	public Object clone() { 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Item e2 = itemRegistry.createByName(itemRegistry.getClassName(this.getClass()), new Class[] {}, new Object[] {});
			e2.deserialize(this.serialize());
			return e2;
		} 
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
