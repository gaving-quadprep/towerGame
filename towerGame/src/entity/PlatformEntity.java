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
		sd.setObject(this.canBeStoodOn, "canBeStoodOn");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.canBeStoodOn = (Boolean)sd.getObjectDefault("canBeStoodOn", false);
	}
}
