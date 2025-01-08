package entity;

import java.awt.Color;
import java.awt.Rectangle;

import main.Main;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Position;

public class Explosion extends Entity {
	double size;
	int explosionTimer, originalExplosionTimer;
	Color color = new Color(237, 164, 5);
	public Explosion(Level level, double size) {
		super(level);
		this.size = size;
		explosionTimer = originalExplosionTimer = (int) (50 + (size * 3));
		this.hitbox = new Rectangle(-8, -8, 16, 16);
		
		
	}
	public Explosion(Level level) {
		this(level, 1.5);
	}
	private int getTransparency() {
		return (int) (255 * ((float) explosionTimer / (float) originalExplosionTimer));
	}
	public void render(WorldRenderer wr) {
		wr.fillEllipse(this.x - size, this.x + size, this.y - size, this.x + size, new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.max(0, this.getTransparency())));
	}
	public void update() {
		super.update();
		this.explosionTimer--;
		if(this.explosionTimer <= 0) {
			this.markedForRemoval = true;
		}
	}
	public String getDebugString() {
		String str = String.valueOf(size) + '\n' + String.valueOf(explosionTimer);
		
		return str;
	}
}
