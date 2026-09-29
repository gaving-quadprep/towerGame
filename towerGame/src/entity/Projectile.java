package entity;

import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class Projectile extends GravityAffectedEntity {
	public boolean hasBeenReflected = false;
	public long createTime;
	public Projectile(Level level) {
		super(level);
		createTime = System.currentTimeMillis();
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update() {
		super.update();
		level.forEachEntityOfType(LivingEntity.class, true, (e) -> {
			if(shouldDamage(e) && CollisionChecker.checkEntities(this, e)) {
				doDamageTo((e), getDamage());
				markedForRemoval = true;
			}
		});
	}
	@Override
	public void onHit(Direction direction) {
		super.onHit(direction);
		x += xVelocity;
		y += yVelocity;
		if(breaksTiles()) {
			int[] positions=CollisionChecker.getTilePositions(level, this, Direction.LEFT, 0);

			level.destroyIfCracked(positions[0], positions[2], true);
			level.destroyIfCracked(positions[1], positions[2], true);
			level.destroyIfCracked(positions[0], positions[3], true);
			level.destroyIfCracked(positions[1], positions[3], true);
		}
		markedForRemoval = true;
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
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(createTime, "createTime");
		sd.setObject(hasBeenReflected, "hasBeenReflected");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		createTime = (long)sd.getObjectDefault("createTime",-1);
		hasBeenReflected = (boolean)sd.getObjectDefault("hasBeenReflected",false);
	}

}
