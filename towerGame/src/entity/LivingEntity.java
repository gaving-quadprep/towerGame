package entity;

import java.awt.Graphics2D;

import main.Main;
import map.Level;
import save.SerializedData;

public class LivingEntity extends GravityAffectedEntity {
	public double health;
	public double maxHealth;
	public int damageTimer;
	public int damageCooldown=10;
	public boolean shouldRenderHealthBar = true;
	public LivingEntity(Level level) {
		super(level);
	}
	public void update() {
		super.update();
		
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
	}
	public void damage(double damage) {
		if(this.damageTimer==0) {
			this.health-=damage;
			if(this.health<=0) {
				this.markedForRemoval=true;
			}
			this.damageTimer=damageCooldown;
		}
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if((positions[0]+(16*Main.scale) > 0 && positions[0] < 320*Main.scale)&&(positions[1]+(16*Main.scale) > 0 && positions[1] < 240*Main.scale))
			g2.drawImage(this.sprite,positions[0],positions[1],Main.tileSize,Main.tileSize,null);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.health, "health");
		sd.setObject(this.maxHealth, "maxHealth");
		sd.setObject(this.damageTimer, "damageTimer");
		sd.setObject(this.damageCooldown, "damageCooldown");
		sd.setObject(this.shouldRenderHealthBar, "shouldRenderHealthBar");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.maxHealth = (double)sd.getObjectDefault("maxHealth",10);
		this.health = (double)sd.getObjectDefault("health",this.maxHealth);
		this.damageTimer = (int)sd.getObjectDefault("damageTimer",0);
		this.damageCooldown = (int)sd.getObjectDefault("damageCooldown",10);
		this.shouldRenderHealthBar = (boolean)sd.getObjectDefault("shouldRenderHealthBar", true);
	}
}