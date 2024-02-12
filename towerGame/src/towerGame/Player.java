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
	public double mana=15.0f;
	public double armor=0.0f;
	public int weapon;
	public int coins;
	public List<Integer> weapons = new ArrayList<Integer>();
	BufferedImage swordSprite;
	boolean swordSwing=false;
	public Direction facing = Direction.RIGHT;
	public Item[] inventory = new Item[16];
	public Item swordSlot;
	public Item armorSlot;
	public Player(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(1,1,15,15);
		this.x=level.playerStartX;
		this.y=level.playerStartY;
		this.airResistance = 1.04;
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
		super.update();
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
		if(Math.abs(this.xVelocity) < 0.01) {
			this.xVelocity = 0;
		}
		//heal
		if(level.healPlayer && ((Main.frames % 720) == 0) && (this.health+0.1) <= this.maxHealth) {
			this.health += 0.1;
		}
		if(eventHandler!=null) {
			if(eventHandler.upPressed&&this.onGround) {
				this.yVelocity=-0.1582F;
				if(CollisionChecker.checkSpecificTile(this.level, this, Direction.LEFT, 0, Tile.jumpPad)) {
					this.yVelocity-=0.0342F;
				}
			};
			if(eventHandler.leftPressed) {
				this.facing=Direction.LEFT;
				
				CollisionChecker.checkForTileTouch(this.level, this, Direction.LEFT, 0.052);
				if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.052)) {
					this.x-=0.052;
				}else {
					if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.052/4)) {
						this.x-=0.052/4;
					}
				}
				this.xVelocity -= 0.001;
			}
			if(eventHandler.rightPressed) {
				this.facing=Direction.RIGHT;
				CollisionChecker.checkForTileTouch(this.level, this, Direction.RIGHT, 0.052);
				if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.052)) {
					this.x+=0.052;
				}else {
					if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.052/4)) {
						this.x+=0.052/4;
					}
				}
				this.xVelocity += 0.001;
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