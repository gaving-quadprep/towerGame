package entity;

import map.Level;
import map.Tile;
import save.SerializedData;
import sound.SoundManager;
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
		for(Entity e : this.level.getAllEntities()) {
			if( e instanceof LivingEntity) {
				if(shouldDamage(e)) {
					if(CollisionChecker.checkEntities(this, e)) {
						((LivingEntity) e).damage(this.getDamage());
						this.markedForRemoval = true;
					}
				}
			}
		}
	}
	public void onHit(Direction direction) {
		super.onHit(direction);
		this.x += this.xVelocity;
		this.y += this.yVelocity;
		if(this.breaksTiles()) {
			int[] positions=CollisionChecker.getTilePositions(this.level, this, Direction.LEFT, 0);
			if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[2]))) {
				this.level.destroy(positions[0], positions[2]);
				SoundManager.play("boulder.wav", 0);
			}
			if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[2]))) {
				this.level.destroy(positions[1], positions[2]);
				SoundManager.play("boulder.wav", 0);
			}
			if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[3]))) {
				this.level.destroy(positions[0], positions[3]);
				SoundManager.play("boulder.wav", 0);
			}
			if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[3]))) {
				this.level.destroy(positions[1], positions[3]);
				SoundManager.play("boulder.wav", 0);
			}
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
