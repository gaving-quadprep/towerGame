package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.Main;
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
	public long createTime;
	public int size;
	public PlayerProjectile(Level level) {
		this(level, null);
	}

	public PlayerProjectile(Level level, Player player) {
		super(level);
		this.player=player;
		this.x=this.player.x;
		this.y=this.player.y;
		this.hitbox=CollisionChecker.getHitbox(7,7,8,8);
		this.size=1;
	}
	public void render(WorldRenderer wr) {
		// Temporary solution
		Graphics2D g2 = wr.getGraphics();
		int posX = (int)(this.x*Main.tileSize-(int)(level.cameraX*Main.tileSize))+7*Main.scale;
		int posY = (int)(this.y*Main.tileSize-(int)(level.cameraY*Main.tileSize))+7*Main.scale;
		g2.setColor(trailColor);
		g2.setStroke(strokeRoundedLine);
		g2.drawLine(posX+Main.scale, posY+Main.scale, (int)(posX-(xVelocity*20*Main.scale)), (int)(posY-(yVelocity*20*Main.scale)));
		g2.setColor(color);
		g2.fillOval(posX,posY,(int)(Main.scale*(1+1.4*this.size)),(int)(Main.scale*(1+1.4*this.size)));
	}
	@Override
	public boolean breaksTiles() {
		return this.size > 2;
	}
	@Override
	public boolean shouldDamage(Entity entity) {
		return !(entity instanceof Player);
	}
	@Override
	public double getDamage() {
		return 1.0F + (0.5F*this.size);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.createTime, "createTime");
		sd.setObject(this.size, "size");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.createTime = (long)sd.getObjectDefault("createTime",-1);
		this.size = (int)sd.getObjectDefault("size",1);
	}
}
