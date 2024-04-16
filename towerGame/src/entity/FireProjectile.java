package entity;

import java.awt.Color;

import main.WorldRenderer;
import map.Level;
import map.Tile;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;
import util.Direction;

public class FireProjectile extends Projectile {
	private static final Color color = new Color(252,71,21);
	public long createTime;
	public boolean isBlue;
	public FireProjectile(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(6,6,10,10);
	}
	public FireProjectile(Level level, boolean isBlue) {
		this(level);
		this.isBlue=isBlue;
	}
	public void update() {
		int[] positions;
		if(CollisionChecker.checkTile(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity)) {
			this.markedForRemoval=true;
			if(this.isBlue) {
				positions=CollisionChecker.getTilePositions(this.level, this, (xVelocity<0)?Direction.LEFT:Direction.RIGHT, (xVelocity<0)?-xVelocity:xVelocity);
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[2]))) {
					this.level.destroy(positions[0], positions[2]);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[2]))) {
					this.level.destroy(positions[1], positions[2]);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[3]))) {
					this.level.destroy(positions[0], positions[3]);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[3]))) {
					this.level.destroy(positions[1], positions[3]);
				}
			}
		}
		this.x+=xVelocity;
		if(CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.markedForRemoval=true;
			if(this.isBlue) {
				positions=CollisionChecker.getTilePositions(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity);
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[2]))) {
					this.level.destroy(positions[0], positions[2]);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[2]))) {
					this.level.destroy(positions[1], positions[2]);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[0], positions[3]))) {
					this.level.destroy(positions[0], positions[3]);
				}
				if(Tile.isCracked(this.level.getTileForeground(positions[1], positions[3]))) {
					this.level.destroy(positions[1], positions[3]);
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
		this.yVelocity+=0.009D;
		if(this.y>500) {
			this.markedForRemoval=true;
		}
		
	}
	public String getSprite() {
		if(this.isBlue) {
			return "bluefireprojectile.png";
		}else {
			return "fireprojectile.png";
		}
	}
	public void render(WorldRenderer wr) {
		//wr.getGraphics().setColor(color);
		wr.drawImage(this.sprite, this.x + 6/16, this.y + 6/16, 4/16, 4/16);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.isBlue, "isBlue");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.isBlue = (boolean)sd.getObjectDefault("isBlue",false);
	}
}
