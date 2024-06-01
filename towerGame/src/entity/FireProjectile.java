package entity;

import java.awt.Color;

import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;

public class FireProjectile extends Projectile {
	private static final Color color = new Color(252,71,21);
	public long createTime;
	public boolean isBlue;
	public FireProjectile(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(6,6,10,10);
	}
	public FireProjectile(Level level, boolean isBlue) {
		this(level);
		this.isBlue=isBlue;
	}
	public String getSprite() {
		if(this.isBlue) {
			return "bluefireprojectile.png";
		}else {
			return "fireprojectile.png";
		}
	}
	@Override
	public boolean breaksTiles() {
		return isBlue;
	}
	@Override
	public boolean shouldDamage(Entity entity) {
		return entity instanceof Player;
	}
	@Override
	public double getDamage() {
		return this.isBlue ? 2.0 : 1.5;
	}
	public void render(WorldRenderer wr) {
		//wr.getGraphics().setColor(color);
		wr.drawImage(this.sprite, this.x + 6d/16, this.y + 6d/16, 4d/16, 4d/16);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.isBlue, "isBlue");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.isBlue = (boolean)sd.getObjectDefault("isBlue",false);
	}
}
