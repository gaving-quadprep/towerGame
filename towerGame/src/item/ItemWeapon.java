package item;

import save.SerializedData;

public class ItemWeapon extends Item {
	int weaponId;
	public ItemWeapon() {}
	public ItemWeapon(int weapon) {
		weaponId = weapon;
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(weaponId, "weaponId");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		weaponId = (int)sd.getObjectDefault("weaponId", 0);
	}
}
