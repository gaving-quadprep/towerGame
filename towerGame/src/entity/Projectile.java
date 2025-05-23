package entity;

import map.Level;
import map.Tile;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class Projectile extends GravityAffectedEntity {
	public boolean hasBeenReflected = false;
	public long createTime;
	public Projectile(Level level) {
		super(level);
		this.createTime = System.currentTimeMillis();
		// TODO Auto-generated constructor stub
	}
	public void update() {
		super.update();
		level.forEachEntityOfType(LivingEntity.class, true, (e) -> {
			if(shouldDamage(e)) {
				if(CollisionChecker.checkEntities(this, e)) {
					((LivingEntity) e).damage(this.getDamage());
					this.markedForRemoval = true;
				}
			}
		});
	}
	public void onHit(Direction direction) {
		super.onHit(direction);
		this.x += this.xVelocity;
		this.y += this.yVelocity;
		if(this.breaksTiles()) {
			int[] positions=CollisionChecker.getTilePositions(this.level, this, Direction.LEFT, 0);
			
			this.level.destroyIfCracked(positions[0], positions[2], true);
			this.level.destroyIfCracked(positions[1], positions[2], true);
			this.level.destroyIfCracked(positions[0], positions[3], true);
			this.level.destroyIfCracked(positions[1], positions[3], true);
		}
		this.markedForRemoval = true;
	}
	public boolean breaksTiles() {
		return false;
	}
	public boolean shouldDamage(Entity entity) {
		return true;
	}
	public double getDamage() {
		return 1;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.createTime, "createTime");
		sd.setObject(this.hasBeenReflected, "hasBeenReflected");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.createTime = (long)sd.getObjectDefault("createTime",-1);
		this.hasBeenReflected = (boolean)sd.getObjectDefault("hasBeenReflected",false);
	}

}
