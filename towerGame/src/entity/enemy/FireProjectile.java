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
		hitbox = CollisionChecker.getHitbox(6, 6, 10, 10);
	}
	public FireProjectile(Level level, boolean isBlue) {
		this(level);
		this.isBlue = isBlue;
	}
	@Override
	public String getSprite() {
		if(isBlue) {
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
		return isBlue ? 2.0 : 1.5;
	}
	@Override
	public void render(WorldRenderer wr) {
		//wr.getGraphics().setColor(color);
		wr.drawImage(sprite, x + 6d/16, y + 6d/16, 4d/16, 4d/16);
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(isBlue, "isBlue");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		isBlue = (boolean)sd.getObjectDefault("isBlue",false);
	}
}
