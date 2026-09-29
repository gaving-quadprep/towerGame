package entity;

import java.awt.Color;
import java.awt.Rectangle;

import main.WorldRenderer;
import map.Level;
import sound.SoundManager;
import util.CollisionChecker;
import util.Direction;

public class Explosion extends Entity {
	double size;
	private boolean started = false;
	int explosionTimer, originalExplosionTimer;
	Color color = new Color(237, 164, 5);
	Color smokeColor = new Color(100, 90, 80);

	public Explosion(Level level, double size) {
		super(level);
		this.size = size;
		explosionTimer = originalExplosionTimer = (int) (50 + (size * 3));
		int scaledSize = (int)(16 * size);
		hitbox = new Rectangle(-scaledSize, -scaledSize, scaledSize * 2, scaledSize * 2);
	}

	public Explosion(Level level) {
		this(level, 1.5);
	}


	public void explode() {
		started = true;
		SoundManager.play("explosion.wav", 0); // no longer crashes :)
		level.forEachEntityOfType(GravityAffectedEntity.class, true, (e) -> {
			double distance = CollisionChecker.distance(this, e);
			if(distance <= size + 1.5) {
				e.xVelocity += ((e.x - x)/distance) / 14;
				e.yVelocity += ((e.y - y)/distance) / 8;
				if (e instanceof LivingEntity)
					doDamageTo(((LivingEntity)e), ((size * 1.5 + 2.5) - distance) * 2);
			}
		});
		int[] positions = CollisionChecker.getTilePositions(level, this, Direction.LEFT, 0);
		for (int x = positions[0]; x < positions[1]; x++) {
			for (int y = positions[2]; y < positions[3]; y++) {
				level.destroyIfCracked(x, y);
			}
		}
	}

	private int getTransparency() {
		return (int) (255 * ((float) explosionTimer / (float) originalExplosionTimer));
	}

	private float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	private Color getColor() {
		float timeScale = (float) explosionTimer / (float) originalExplosionTimer;
		return new Color((int)(lerp(smokeColor.getRed(), color.getRed(), timeScale)),
				(int)(lerp(smokeColor.getGreen(), color.getGreen(), timeScale)),
				(int)(lerp(smokeColor.getBlue(), color.getBlue(), timeScale)),
				Math.max(0, getTransparency()));
	}

	@Override
	public void render(WorldRenderer wr) {
		if(started)
			wr.fillEllipse(x - size, x + size, y - size, y + size, getColor());
	}

	@Override
	public void update() {
		super.update();
		if(started) {
			explosionTimer--;
			if(explosionTimer <= 0) {
				markedForRemoval = true;
			}
		}
	}

	@Override
	public String getDebugString() {
		String str = started ? String.valueOf(size) + '\n' + String.valueOf(explosionTimer) : "NOT STARTED";

		return str;
	}
}
