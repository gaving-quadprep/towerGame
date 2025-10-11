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
	
	public void update() {
		super.update();
		this.shouldRenderHealthBar = !invulnerable;
	}
	
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.dialog, "dialog");
		return sd;
	}
	
	@SuppressWarnings("unchecked")
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.dialog = (List<String>)sd.getObjectDefault("dialog",new ArrayList<String>());
		this.invulnerable = (Boolean)sd.getObjectDefault("killable",false);
	}

}
