package entity;

import java.util.ArrayList;
import java.util.List;

import map.Level;
import save.SerializedData;

public class NPC extends LivingEntity {
	public boolean killable;
	public List<String> dialog;
	public NPC(Level level) {
		this(level, false);
	}
	public NPC(Level level, boolean killable) {
		super(level);
		this.killable = killable;
		this.shouldRenderHealthBar = !killable;
	}
	public void damage(double damage) {
		if(this.killable) {
			super.damage(damage);
		}
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.dialog, "dialog");
		sd.setObject(this.killable, "killable");
		return sd;
	}
	@SuppressWarnings("unchecked")
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.dialog = (List<String>)sd.getObjectDefault("dialog",new ArrayList<String>());
		this.killable = (boolean)sd.getObjectDefault("killable",false);
	}

}
