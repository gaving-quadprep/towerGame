package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;

public class PlayerProjectile extends Projectile {
	private static final BasicStroke strokeRoundedLine = new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
	private static final Color color = new Color(227, 216, 176);
	private static final Color trailColor = new Color(12, 187, 250, 22);
	private Player player;
	public int size;
	public PlayerProjectile(Level level) {
		this(level, null);
	}

	public PlayerProjectile(Level level, Player player) {
		super(level);
		this.player=player;
		x=this.player.x;
		y=this.player.y;
		hitbox=CollisionChecker.getHitbox(7,7,8,8);
		size=1;
	}
	@Override
	public void render(WorldRenderer wr) {
		double drawSize = (0.0625 + 0.0875 * size);
		wr.fillEllipse(x + 0.5, x + 0.5 + drawSize, y + 0.5, y + 0.5 + drawSize, color);
	}
	@Override
	public boolean breaksTiles() {
		return size > 2;
	}
	@Override
	public boolean shouldDamage(Entity entity) {
		return !(entity instanceof Player);
	}
	@Override
	public double getDamage() {
		return 1.0F + (0.5F*size);
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(createTime, "createTime");
		sd.setObject(size, "size");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		createTime = (long)sd.getObjectDefault("createTime",-1);
		size = (int)sd.getObjectDefault("size",1);
	}
}
