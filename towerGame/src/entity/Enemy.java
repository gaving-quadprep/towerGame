package entity;

import main.CollisionChecker;
import map.Level;
import save.SerializedData;

public class Enemy extends LivingEntity{
	public double attackDamage;
	public int attackCooldown;
	public Enemy(Level level) {
		super(level);
		this.damageCooldown = 4;
		this.attackDamage = 1.0F;
	}
	public void update() {
		super.update();
		if(this.level.player!=null) {
			if(CollisionChecker.checkEntities(this,this.level.player)) {
				this.level.player.damage(this.attackDamage);
			}
		}
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.attackDamage, "attackDamage");
		sd.setObject(this.attackCooldown, "attackCooldown");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.attackDamage = (double)sd.getObjectDefault("attackDamage",1);
		this.attackCooldown = (int)sd.getObjectDefault("attackCooldown",0);
	}

}
