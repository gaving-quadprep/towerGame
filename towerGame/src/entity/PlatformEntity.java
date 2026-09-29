package entity;

import map.Level;
import save.SerializedData;

public abstract class PlatformEntity extends GravityAffectedEntity {
	public PlatformEntity(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}

	public boolean canBeStoodOn;

	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(canBeStoodOn, "canBeStoodOn");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		canBeStoodOn = (boolean)sd.getObjectDefault("canBeStoodOn", false);
	}
}
