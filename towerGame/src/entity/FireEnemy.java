package entity;

import java.awt.Rectangle;
import java.math.BigDecimal;

import main.Main;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;

public class FireEnemy extends Enemy {
	public boolean isBlue;
	public double baseY;
	public FireEnemy(Level level, boolean isBlue) {
		super(level);
		this.attackCooldown = 180;
		this.isBlue = isBlue;
		this.hitbox = new Rectangle(0, 0, 16, 16);
		this.attackDamage = this.isBlue ? 1.5 : 1.0;
		this.maxHealth = this.isBlue ? BigDecimal.valueOf(12.5) : BigDecimal.TEN;
		if(this.isBlue) {
			this.attackDamage += 0.5D;
		}
		this.health = this.maxHealth;
	}
	public FireEnemy(Level level) {
		this(level,false);
	}
	public void update() {
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
		if(this.level.player!=null) {
			if(CollisionChecker.checkEntities(this,this.level.player)) {
				this.level.player.damage(this.attackDamage);
			}
		}
		this.y = baseY+(double) Math.sin(((double)Main.frames)/30.0D);
		if(this.attackCooldown <= 0) {
			if(CollisionChecker.distanceTaxicab(this, level.player) < 15) {
				double angle=(double)Math.atan2((this.level.player.x)-this.x, this.level.player.y-this.y);
				FireProjectile p = new FireProjectile(this.level, this.isBlue);
				p.xVelocity = (double) Math.sin(angle)/4.5D;
				p.yVelocity = (double) (Math.cos(angle)/4.5D)-0.1D - (0.002D * Math.abs(this.level.player.x-this.x));
				p.setPosition(this.x, this.y);
				this.level.addEntity(p);
				this.attackCooldown = (int)(Math.random() * (this.isBlue ? 150 : 200)) + 50;
			}
		}else {
			this.attackCooldown--;
		}
	}

	public String getSprite() {
		if(this.isBlue) {
			return "enemy/bluefiresprite.png";
		} else {
			return "enemy/redfiresprite.png";
		}
	}
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		this.baseY=y;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.isBlue, "isBlue");
		sd.setObject(this.baseY, "baseY");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.isBlue = (boolean)sd.getObjectDefault("isBlue",false);
		this.baseY = (double)sd.getObjectDefault("baseY",this.y);
	}
}