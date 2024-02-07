package towerGame;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import entity.LivingEntity;
import item.Item;
import main.CollisionChecker;
import main.Direction;
import main.Main;
import map.Level;
import map.Tile;
import weapon.Weapon;

public class Player extends LivingEntity {
	public double xVelocity;
	public double yVelocity;
	public boolean onGround=false;
	public double mana=15.0f;
	public double armor=0.0f;
	public int weapon;
	public int coins;
	public List<Integer> weapons = new ArrayList<Integer>();
	BufferedImage swordSprite;
	boolean swordSwing=false;
	public Direction facing = Direction.RIGHT;
	public Item[] inventory;
	public Player(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(1,1,15,15);
		this.x=level.playerStartX;
		this.y=level.playerStartY;
		this.maxHealth=10.0f;
		this.damageCooldown=15;
		this.health=this.maxHealth;
		this.weapon = Weapon.staff.id;
		this.swordSprite=level.getSprite(Weapon.weapons[this.weapon].texture);
	}
	public String getSprite() {
		return "player.png";
	}
	public void update(EventHandler eventHandler) {
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
		if(eventHandler!=null) {
			if(eventHandler.upPressed&&this.onGround) {this.yVelocity=-0.158F;};
			if(eventHandler.leftPressed) {
				this.facing=Direction.LEFT;
				if(CollisionChecker.checkSpecificTiles(this.level, this, Direction.LEFT, 0.052F,Tile.damage_tiles)) {
					this.health=0;
				}
				if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.052F)) {
					this.x-=0.052;
				}else {
					if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.052F/3)) {
						this.x-=0.052F/3;
					}
					if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.052F/7)) {
						this.x-=0.052F/7;
					}
				}
			}
			if(eventHandler.rightPressed) {
				this.facing=Direction.RIGHT;
				if(CollisionChecker.checkSpecificTiles(this.level, this, Direction.RIGHT, 0.052F, Tile.damage_tiles)) {
					this.health=0;
				}
				if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.052F)) {
					this.x+=0.052;
				}else {
					if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.052F/3)) {
						this.x+=0.052F/3;
					}
					/*if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.052F/7)) {
						this.x+=0.052F/7;
					}*/
				}
			}
			if(eventHandler.mouse1Pressed || eventHandler.mouse2Pressed) {
				Point mousePos= MouseInfo.getPointerInfo().getLocation();
				Weapon.weapons[this.weapon].onMouseHeld(level, this, mousePos.x, mousePos.y);
				this.swordSwing=true;
			}else {
				this.swordSwing=false;
			}
			if(eventHandler.mouse1Clicked) {
				Point mousePos= MouseInfo.getPointerInfo().getLocation();
				Weapon.weapons[this.weapon].onAttack(level, this, false, mousePos.x, mousePos.y);
			}
			if(eventHandler.mouse2Clicked) {
				Point mousePos= MouseInfo.getPointerInfo().getLocation();
				Weapon.weapons[this.weapon].onAttack(level, this, true, mousePos.x, mousePos.y);
			}
		}
		this.x+=xVelocity;
		this.yVelocity+=0.007F;//gravity

		if(CollisionChecker.checkSpecificTiles(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.damage_tiles)) {
			this.health=0;
		}

		if(CollisionChecker.checkSpecificTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.conveyorLeft)) {
			if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.075F)) {
				this.x-=0.075;
			}
		}
		if(CollisionChecker.checkSpecificTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity, Tile.conveyorRight)) {
			if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.075F)) {
				this.x+=0.075;
			}
		}
		if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, (yVelocity<0)?-yVelocity:yVelocity)) {
			this.y+=yVelocity;
			this.onGround=false;
		}else {

			if(!CollisionChecker.checkTile(this.level, this, (yVelocity<0)?Direction.UP:Direction.DOWN, ((yVelocity<0)?-yVelocity:yVelocity)/3)) {
				this.y+=yVelocity/3;
			}
			if(this.yVelocity>0) {
				this.onGround=true;
			}else {
				this.onGround=false;
			}
			this.yVelocity=yVelocity>0?-(this.yVelocity/8):-(this.yVelocity); //bounce
		}
		int[] positions = CollisionChecker.getTilePositions(level, this, Direction.LEFT, 0);
		if(this.level.getTileForeground(positions[0], positions[2])==Tile.checkpoint.id) {
			TowerGame.playerCheckpointX=(int)positions[0];
			TowerGame.playerCheckpointY=(int)positions[2];
		}
		if(this.level.getTileForeground(positions[0], positions[2])==Tile.exit.id) {
			TowerGame.hasWon = true;
		}
		if(this.level.getTileForeground(positions[1], positions[2])==Tile.checkpoint.id) {
			TowerGame.playerCheckpointX=(int)positions[1];
			TowerGame.playerCheckpointY=(int)positions[2];
		}
		if(this.level.getTileForeground(positions[1], positions[2])==Tile.exit.id) {
			TowerGame.hasWon = true;
		}
		if(this.level.getTileForeground(positions[0], positions[3])==Tile.checkpoint.id) {
			TowerGame.playerCheckpointX=(int)positions[0];
			TowerGame.playerCheckpointY=(int)positions[3];
		}
		if(this.level.getTileForeground(positions[0], positions[3])==Tile.exit.id) {
			TowerGame.hasWon = true;
		}
		if(this.level.getTileForeground(positions[1], positions[3])==Tile.checkpoint.id) {
			TowerGame.playerCheckpointX=(int)positions[1];
			TowerGame.playerCheckpointY=(int)positions[3];
		}
		if(this.level.getTileForeground(positions[1], positions[3])==Tile.exit.id) {
			TowerGame.hasWon = true;
		}
		if(this.y > level.sizeY + 40) {
			this.health = 0;
		}
	}
	public void render(Graphics2D g2) {
		if(this.facing==Direction.LEFT) {
			g2.drawImage(this.sprite,(int)Math.round(this.x*Main.tileSize+Main.tileSize-(int)(level.cameraX*Main.tileSize)),(int)Math.round(this.y*Main.tileSize-(int)(level.cameraY*Main.tileSize)),-Main.tileSize,Main.tileSize,null);
			g2.drawImage(this.swordSprite, (int)(x*Main.tileSize-0.5*Main.tileSize)-(int)(level.cameraX*Main.tileSize), (int)(y*Main.tileSize)-(int)(level.cameraY*Main.tileSize), (int)(x*Main.tileSize+0.5*Main.tileSize)-(int)(level.cameraX*Main.tileSize), (int)(y*Main.tileSize)+Main.tileSize-(int)(level.cameraY*Main.tileSize),16, this.swordSwing?16:0, 0, this.swordSwing?32:16, (ImageObserver)null);
		} else {
			g2.drawImage(this.sprite,(int)Math.round(this.x*Main.tileSize-(int)(level.cameraX*Main.tileSize)),(int)Math.round(this.y*Main.tileSize-(int)(level.cameraY*Main.tileSize)),Main.tileSize,Main.tileSize,null);
			g2.drawImage(this.swordSprite, (int)(x*Main.tileSize+0.5*Main.tileSize)-(int)(level.cameraX*Main.tileSize), (int)(y*Main.tileSize)-(int)(level.cameraY*Main.tileSize), (int)(x*Main.tileSize+1.5*Main.tileSize)-(int)(level.cameraX*Main.tileSize), (int)(y*Main.tileSize)+Main.tileSize-(int)(level.cameraY*Main.tileSize),0, this.swordSwing?16:0, 16, this.swordSwing?32:16, (ImageObserver)null);
		}
	}
	public void renderDebug(Graphics2D g2) {
	}
	public void damage(double damage) {
		super.damage(damage/(1+this.armor));
	}
	public void setWeapon(int id) {
		this.weapon = id;
		this.swordSprite=level.getSprite(Weapon.weapons[this.weapon].texture);
		
	}
}