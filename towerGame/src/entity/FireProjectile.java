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

public class FireProjectile extends Entity {
	public double xVelocity;
	public double yVelocity;
	public long createTime;
	public boolean isBlue;
	public boolean hasBeenReflected = false;
	public FireProjectile(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(6,6,10,10);
	}
	public FireProjectile(Level level, boolean isBlue) {
		this(level);
		this.isBlue=isBlue;
	}
	@Override
	public void update() {
		int[] positions;
		if(CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
			this.markedForRemoval=true;
			if(this.isBlue) {
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
			if(this.isBlue) {
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
		Player p = this.level.player;
		if(!this.hasBeenReflected) {
			if(CollisionChecker.checkEntities(this, p)) {
				p.damage(this.isBlue ? 2.0f : 1.5f);
				this.markedForRemoval=true;
			}
		}
		this.y+=yVelocity;
		this.yVelocity+=0.009F;
		if(this.y>500) {
			this.markedForRemoval=true;
		}
		
	}
	@Override
	public String getSprite() {
		if(this.isBlue) {
			return "bluefireparticle.png";
		}else {
			return "fireparticle.png";
		}
	}
	@Override
	public void render(Graphics2D g2) {
		g2.setColor(new Color(252,71,21));
		g2.drawImage(this.sprite,(int)Math.round(this.x*Main.tileSize-(int)(level.cameraX*Main.tileSize))+6*Main.scale,(int)Math.round(this.y*Main.tileSize-(int)(level.cameraY*Main.tileSize))+6*Main.scale,4*Main.scale,4*Main.scale,null);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.xVelocity, "xVelocity");
		sd.setObject(this.yVelocity, "yVelocity");
		sd.setObject(this.createTime, "createTime");
		sd.setObject(this.hasBeenReflected, "hasBeenReflected");
		sd.setObject(this.isBlue, "isBlue");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.xVelocity = (double)sd.getObjectDefault("xVelocity",0);
		this.yVelocity = (double)sd.getObjectDefault("yVelocity",0);
		this.createTime = (long)sd.getObjectDefault("createTime",-1);
		this.hasBeenReflected = (boolean)sd.getObjectDefault("hasBeenReflected",false);
		this.isBlue = (boolean)sd.getObjectDefault("isBlue",false);
	}
}
