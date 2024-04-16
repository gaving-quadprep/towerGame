package towerGame;

import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import entity.LivingEntity;
import item.Item;
import main.Main;
import main.WorldRenderer;
import map.Level;
import map.Tile;
import util.CollisionChecker;
import util.Direction;
import weapon.Spell;
import weapon.Weapon;

public class Player extends LivingEntity {
	public BigDecimal mana = BigDecimal.valueOf(15, 0);
	public double armor=0.0f;
	public int weapon;
	public int coins;
	BufferedImage swordSprite;
	boolean swordSwing=false;
	public Direction facing = Direction.RIGHT;
	public Item[] inventory = new Item[15];
	public Item swordSlot;
	public Item armorSlot;
	public List<Spell> spells = new ArrayList<Spell>();
	public Spell equippedSpell;
	public Player(Level level) {
		super(level);
		this.hitbox=CollisionChecker.getHitbox(1,1,15,15);
		this.x=level.playerStartX;
		this.y=level.playerStartY;
		this.airResistance = 1.04;
		this.maxHealth=BigDecimal.TEN;
		this.damageCooldown=15;
		this.health=this.maxHealth;
		this.weapon = Weapon.staff.id;
		this.swordSprite=level.getSprite("weapon/"+Weapon.weapons[this.weapon].texture);
	}
	public boolean addToInventory(Item item) {
		for(int i=0;i<15;i++) {
			if(inventory[i] == null) {
				inventory[i] = item;
				item.sprite = level.getSprite(item.getSprite());
				return true;
			}
		}
		return false;
	}
	public String getSprite() {
		return "player.png";
	}
	public void update(EventHandler eventHandler) {
		super.update();
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
		if(Math.abs(this.xVelocity) < 0.00001) {
			this.xVelocity = 0;
		}
		//heal
		if(level.healPlayer && ((Main.frames % 720) == 0) && (this.health.add(Main.ONE_TENTH)).compareTo(maxHealth) <= 0) {
			this.health = this.health.add(Main.ONE_TENTH);
		}
		if(eventHandler!=null) {
			if(eventHandler.upPressed&&this.onGround) {
				this.yVelocity=-0.1582F;
				if(CollisionChecker.checkSpecificTile(this.level, this, Direction.DOWN, 0, Tile.jumpPad) || CollisionChecker.checkSpecificTile(this.level, this, Direction.UP, 0, Tile.jumpPad)) {
					this.yVelocity-=0.0342F;
				}
			};
			if(eventHandler.leftPressed) {
				this.facing=Direction.LEFT;
				
				CollisionChecker.checkForTileTouch(this.level, this, Direction.LEFT, 0.051);
				if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.051)) {
					this.x -= 0.051;
				}else {
					if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.051/4)) {
						this.x -= 0.051/4;
					}else {
						this.y -= 0.5625;
						if(!CollisionChecker.checkTile(this.level, this, Direction.LEFT, 0.051) && onGround) {
							this.x -= 0.051;
							this.y += 0.46;
						}else {
							this.y += 0.5625;
						}
					}
				}
				this.xVelocity -= 0.00051;
			}
			if(eventHandler.rightPressed) {
				this.facing=Direction.RIGHT;
				CollisionChecker.checkForTileTouch(this.level, this, Direction.RIGHT, 0.051);
				if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.051)) {
					this.x+=0.051;
				}else {
					if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.051/4)) {
						this.x+=0.051/4;
					}else {
						this.y -= 0.5625;
						if(!CollisionChecker.checkTile(this.level, this, Direction.RIGHT, 0.051) && onGround) {
							this.x+=0.051;
							this.y += 0.46;
						}else {
							this.y += 0.5625;
						}
					}
				}
				this.xVelocity += 0.00051;
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
			this.health = BigDecimal.ZERO;
		}
	}
	public void render(WorldRenderer wr) {
		if(this.facing==Direction.LEFT) {
			wr.drawImage(this.sprite, this.x+1, this.y, -1, 1);
			wr.drawTiledImage(this.swordSprite, this.x-0.5, this.y, 1, 1, 16, this.swordSwing?16:0, 0, this.swordSwing?32:16);
		} else {
			wr.drawImage(this.sprite, this.x, this.y, 1, 1);
			wr.drawTiledImage(this.swordSprite, this.x+0.5, this.y, 1, 1, 0, this.swordSwing?16:0, 16, this.swordSwing?32:16);
		}
	}
	public void renderDebug(Graphics2D g2) {
	}
	public void damage(double damage) {
		super.damage(damage/(1+this.armor));
	}
	public void setWeapon(int id) {
		this.weapon = id;
		this.swordSprite=level.getSprite("weapon/"+Weapon.weapons[this.weapon].texture);
		
	}
}