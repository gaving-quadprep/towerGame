package entity;

import java.awt.Color;
import main.WorldRenderer;
import map.Level;
import towerGame.Player;

public class TrackingPlayerProjectile extends PlayerProjectile {
	private static final Color color = new Color(234, 132, 218);
	Entity trackingEntity;
	
	public TrackingPlayerProjectile(Level level, Entity trackingEntity) {
		this(level, trackingEntity, null);
	}

	public TrackingPlayerProjectile(Level level, Entity trackingEntity, Player player) {
		super(level, player);
		this.trackingEntity = trackingEntity;
	}
	
	public void render(WorldRenderer wr) {
		double drawSize = (0.0625 + 0.0875 * size);
		wr.fillEllipse(x + 0.5, x + 0.5 + drawSize, y + 0.5, y + 0.5 + drawSize, color);
	}
	
	@Override
	public boolean shouldDamage(Entity entity) {
		return !(entity instanceof Player);
	}
	
	public void update() {
		super.update();
		this.yVelocity = 0;
		double distX = trackingEntity.x - x;
		double distY = trackingEntity.y - y;
		double xSign = distX < 0 ? -1.0 : 1.0;
		double ySign = distY < 0 ? -1.0 : 1.0;
		this.x = this.x + (Math.max(Math.abs(distX), 2) * xSign * 0.02);
		this.y = this.y + (Math.max(Math.abs(distY), 2) * ySign * 0.02);
	}
}
