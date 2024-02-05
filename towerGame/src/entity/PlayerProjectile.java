package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.CollisionChecker;
import main.Direction;
import main.Main;
import map.Level;
import map.Tile;
import save.SerializedData;
import towerGame.Player;

public class PlayerProjectile extends Entity {
	public double xVelocity;
	public double yVelocity;
	public Player player;
	public long createTime;
	public int size;
	public PlayerProjectile(Level level) {
		this(level,null);
	}

	public PlayerProjectile(Level level, Player player) {
		super(level);
		this.player=player;
		this.x=this.player.x;
		this.y=this.player.y;
		this.hitbox=CollisionChecker.getHitbox(7,7,8,8);
		this.size=1;
	}
	public void update() {
		int[] positions;
		if(CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
			this.markedForRemoval=true;
			if(this.size>2) {
				positions=CollisionChecker.getTilePositions(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity);
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[2]))) {
					this.level.setTileForeground(positions[0], positions[2],0);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[2]))) {
					this.level.setTileForeground(positions[1], positions[2],0);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[3]))) {
					this.level.setTileForeground(positions[0], positions[3],0);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[3]))) {
					this.level.setTileForeground(positions[1], positions[3],0);
				}
			}
		}
		this.x+=xVelocity;
		if(CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.markedForRemoval=true;
			if(this.size>2) {
				positions=CollisionChecker.getTilePositions(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity);
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[2]))) {
					this.level.setTileForeground(positions[0], positions[2],0);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[2]))) {
					this.level.setTileForeground(positions[1], positions[2],0);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[3]))) {
					this.level.setTileForeground(positions[0], positions[3],0);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[3]))) {
					this.level.setTileForeground(positions[1], positions[3],0);
				}
			}
		}
		for(Entity e : this.level.entities) {
			if( e instanceof LivingEntity) {
				if(CollisionChecker.checkEntities(this, e)) {
					((LivingEntity) e).damage(1.0F + (0.5F*this.size));
					this.markedForRemoval=true;
				}
			}
		}
		this.y+=yVelocity;
		this.yVelocity+=0.009F;
		if(this.y>500) {
			this.markedForRemoval=true;
		}
		
	}
	public void render(Graphics2D g2) {
		g2.setColor(new Color(227, 216, 177));
		g2.fillOval((int)(this.x*Main.tileSize-(int)(level.cameraX*Main.tileSize))+7*Main.scale,(int)(this.y*Main.tileSize-(int)(level.cameraY*Main.tileSize))+7*Main.scale,(int)(Main.scale*(1+1.4*this.size)),(int)(Main.scale*(1+1.4*this.size)));
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.xVelocity, "xVelocity");
		sd.setObject(this.yVelocity, "yVelocity");
		sd.setObject(this.createTime, "createTime");
		sd.setObject(this.size, "size");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		this.yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		this.createTime = (long)sd.getObjectDefault("createTime",-1);
		this.size = (int)sd.getObjectDefault("size",1);
	}
}
