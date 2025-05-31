package entity;

import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

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
		this.canBeStoodOn = (boolean)sd.getObjectDefault("canBeStoodOn", false);
	}
}
