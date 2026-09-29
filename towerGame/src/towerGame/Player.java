package towerGame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import entity.DamageSource;
import entity.LivingEntity;
import item.Item;
import main.Main;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;
import weapon.Spell;
import weapon.Weapon;

public class Player extends LivingEntity {
	public BigDecimal mana = BigDecimal.valueOf(15, 0);
	public double armor = 0.0f;
	public int weapon;
	public int coins;
	public double speed = 1d;
	BufferedImage swordSprite;
	boolean swordSwing = false;
	public Item[] inventory = new Item[15];
	public Item swordSlot;
	public Item armorSlot;
	public List<Spell> spells = new ArrayList<>();
	public Spell equippedSpell;

	public Player(Level level) {
		super(level);
		hitbox = CollisionChecker.getHitbox(1,1,15,15);
		x = level.playerStartX;
		y = level.playerStartY;
		airResistance = 1.04;
		maxHealth = BigDecimal.TEN;
		damageCooldown = 15;
		weapon = Weapon.staff.id;
		swordSprite = level.getSprite("weapon/"+Weapon.weapons[weapon].texture);
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

	@Override
	public String getSprite() {
		return "player.png";
	}
	@Override
	public void loadSprites() {
		super.loadSprites();
		if(weapon != 0)
			swordSprite = level.getSprite("weapon/" + Weapon.weapons[weapon].texture);
	}
	public void update(EventHandler eventHandler) {
		super.update();

		if(damageTimer != 0) {
			damageTimer--;
		}
		if(Math.abs(xVelocity) < 0.00001) {
			xVelocity = 0;
		}
		//heal
		if(level.healPlayer && ((Main.frames % 360) == 0) && (health.add(Main.ONE_TENTH)).compareTo(maxHealth) <= 0) {
			health = health.add(Main.ONE_TENTH);
		}
		if(eventHandler!=null) {
			if(eventHandler.upPressed) {
				jump();
			}
			if(eventHandler.leftPressed) {
				this.goLeft(false, speed);
				xVelocity -= 0.00041 * speed;
				if(xVelocity > 0)
					xVelocity -= 0.0003 * speed;
			}
			if(eventHandler.rightPressed) {
				this.goRight(false, speed);
				xVelocity += 0.00041 * speed;
				if(xVelocity < 0)
					xVelocity += 0.0003 * speed;
			}

			if(eventHandler.mouse1Pressed || eventHandler.mouse2Pressed) {
				Point mousePos = eventHandler.getMousePos();
				if(weapon != 0)
					Weapon.weapons[weapon].onMouseHeld(level, this, mousePos.x, mousePos.y);
				swordSwing=true;
			}else {
				swordSwing=false;
			}
			if(eventHandler.mouse1Clicked) {
				Point mousePos = eventHandler.getMousePos();
				if(weapon != 0)
					Weapon.weapons[weapon].onAttack(level, this, false, mousePos.x, mousePos.y);
			}
			if(eventHandler.mouse2Clicked) {
				Point mousePos = eventHandler.getMousePos();
				if(weapon != 0)
					Weapon.weapons[weapon].onAttack(level, this, true, mousePos.x, mousePos.y);
			}
		}
		if(y > level.sizeY + 40) {
			health = BigDecimal.ZERO;
		}
	}
	@Override
	public void render(WorldRenderer wr) {
		if(facing == Direction.LEFT) {
			wr.drawImage(sprite, x+1, y, -1, 1);
			if(weapon != 0)
				wr.drawTiledImage(swordSprite, x-0.5, y, 1, 1, 16, swordSwing?16:0, 0, swordSwing?32:16);
		} else {
			wr.drawImage(sprite, x, y, 1, 1);
			if(weapon != 0)
				wr.drawTiledImage(swordSprite, x+0.5, y, 1, 1, 0, swordSwing?16:0, 16, swordSwing?32:16);
		}
	}
	@Override
	public void renderDebug(Graphics2D g2) {
	}
	@Override
	public void damage(double damage, DamageSource source) {
		super.damage(damage / (1 + armor), source);
	}
	public void setWeapon(int id) {
		weapon = id;
		if(id != 0)
			swordSprite = level.getSprite("weapon/" + Weapon.weapons[weapon].texture);

	}
}