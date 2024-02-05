package entity;

import java.util.ArrayList;
import java.util.List;

import map.Level;
import save.SerializedData;

public class NPC extends LivingEntity {
	public boolean killable;
	public List<String> dialog;
	public NPC(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
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
