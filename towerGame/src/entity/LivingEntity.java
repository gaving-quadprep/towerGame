package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import main.CollisionChecker;
import main.Direction;
import main.EntityRegistry;
import map.Level;
import map.Tile;
import towerGame.TowerGame;

public class LivingEntity extends Entity {
	public float health;
	public float maxHealth;
	public int damageTimer;
	public int damageCooldown=15;
	public float xVelocity;
	public float yVelocity;
	public boolean onGround=false;
	public LivingEntity(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(0,0,16,16);
	}
	public void update() {
		super.update();
		this.yVelocity+=0.007F;//gravity
		if(CollisionChecker.checkSpecificTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.conveyorLeft)) {
			if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.075F)) {
				this.posX-=0.075;
			}
		}
		if(CollisionChecker.checkSpecificTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.conveyorRight)) {
			if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.075F)) {
				this.posX+=0.075;
			}
		}
		if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.posY+=yVelocity;
			this.onGround=false;
		}else {
	
			if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/3)) {
				this.posY+=yVelocity/3;
			}
			if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/7)) {
				this.posY+=yVelocity/7;
			}
			if(this.yVelocity>0) {
				this.onGround=true;
			}else {
				this.onGround=false;
			}
			this.yVelocity=yVelocity>0?-(this.yVelocity/8):-(this.yVelocity); //bounce
		}
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
	}
	public void damage(float damage) {
		if(this.damageTimer==0) {
			this.health-=damage;
			if(this.health<=0) {
				this.markedForRemoval=true;
			}
			this.damageTimer=damageCooldown;
		}
	}
	@Override
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		g2.drawImage(this.sprite,positions[0],positions[1],TowerGame.tileSize,TowerGame.tileSize,null);
	}
}