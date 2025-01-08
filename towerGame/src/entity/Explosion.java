package entity;

import java.awt.Color;
import java.awt.Rectangle;

import main.Main;
import main.WorldRenderer;
import map.Level;
import sound.SoundManager;
import util.CollisionChecker;
import util.Position;

public class Explosion extends Entity {
	double size;
	private boolean started = false;
	int explosionTimer, originalExplosionTimer;
	Color color = new Color(237, 164, 5);
	
	public Explosion(Level level, double size) {
		super(level);
		this.size = size;
		explosionTimer = originalExplosionTimer = (int) (50 + (size * 3));
		this.hitbox = new Rectangle(-8, -8, 16, 16);
	}
	
	public void explode() {
		this.started = true;
		//SoundManager.play("explosion.wav"); //crashes the game for some reason
		double distance;
		for(Entity e : level.getAllEntities()) {
			if(e instanceof LivingEntity) {
				LivingEntity le = (LivingEntity)e;

				distance = CollisionChecker.distance(this, le);
				if(distance <= this.size + 1.5) {
					le.damage((this.size + 5.0) - distance);
					le.xVelocity += ((le.x - x)/distance) / 14;
					le.yVelocity += ((le.y - y)/distance) / 8;
				}
			}
		}
	}
			
	
	public Explosion(Level level) {
		this(level, 1.5);
	}
	
	private int getTransparency() {
		return (int) (255 * ((float) explosionTimer / (float) originalExplosionTimer));
	}
	
	public void render(WorldRenderer wr) {
		if(this.started)
			wr.fillEllipse(this.x - size, this.x + size, this.y - size, this.y + size, new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.max(0, this.getTransparency())));
	}
	
	public void update() {
		super.update();
		if(this.started) {
			this.explosionTimer--;
			if(this.explosionTimer <= 0) {
				this.markedForRemoval = true;
			}
		}
	}
	
	public String getDebugString() {
		String str = this.started ? String.valueOf(size) + '\n' + String.valueOf(explosionTimer) : "NOT STARTED";
		
		return str;
	}
}
