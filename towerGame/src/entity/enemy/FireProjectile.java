package entity.enemy;

import entity.Bomb;
import entity.Entity;
import entity.Projectile;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;

public class FireProjectile extends Projectile {
	public long createTime;
	public boolean isBlue;
	public FireProjectile(Level level) {
		super(level);
		this.hitbox = CollisionChecker.getHitbox(6, 6, 10, 10);
	}
	public FireProjectile(Level level, boolean isBlue) {
		this(level);
		this.isBlue = isBlue;
	}
	public String getSprite() {
		if(this.isBlue) {
			return "bluefireprojectile.png";
		} else {
			return "fireprojectile.png";
		}
	}
	@Override
	public boolean breaksTiles() {
		return isBlue;
	}
	@Override
	public boolean shouldDamage(Entity entity) {
		return entity instanceof Player || entity instanceof Bomb;
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
		this.isBlue = (Boolean)sd.getObjectDefault("isBlue",false);
	}
}
