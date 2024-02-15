package item;

import save.SerializedData;

public class ItemWeapon extends Item {
	int weaponId;
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(weaponId, "weaponId");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.weaponId = (int)sd.getObjectDefault("weaponId", 0);
	}
}
