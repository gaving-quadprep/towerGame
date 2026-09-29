package entity;

import java.util.ArrayList;
import java.util.List;

import map.Level;
import save.SerializedData;

public class NPC extends LivingEntity {
	public List<String> dialog;

	public NPC(Level level) {
		super(level);
	}

	@Override
	public void update() {
		super.update();
		shouldRenderHealthBar = !invulnerable;
	}

	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(dialog, "dialog");
		return sd;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		dialog = (List<String>)sd.getObjectDefault("dialog",new ArrayList<>());
		invulnerable = (boolean)sd.getObjectDefault("killable",false);
	}

}
