package entity.enemy;

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
		attackCooldown = 180;
		this.isBlue = isBlue;
		hitbox = new Rectangle(0, 0, 16, 16);
		attackDamage = this.isBlue ? 1.5 : 1.0;
		maxHealth = this.isBlue ? BigDecimal.valueOf(12.5) : BigDecimal.TEN;
		if(this.isBlue) {
			attackDamage += 0.5D;
		}
		health = maxHealth;
	}
	public FireEnemy(Level level) {
		this(level,false);
	}
	@Override
	public void update() {
		if(damageTimer != 0) {
			damageTimer--;
		}
		if((level.player != null) && CollisionChecker.checkEntities(this, level.player)) {
			doDamageTo(level.player, attackDamage);
		}
		y = baseY+Math.sin((Main.frames)/30.0D);
		if(attackCooldown <= 0) {
			if(CollisionChecker.distanceTaxicab(this, level.player) < 15) {
				double angle=Math.atan2((level.player.x)-x, level.player.y-y);
				FireProjectile p = new FireProjectile(level, isBlue);
				p.xVelocity = Math.sin(angle)/4.5D;
				p.yVelocity = Math.cos(angle)/4.5D - 0.1D - ((isBlue ? 0.007 : 0.004) * Math.abs(level.player.x - x));
				p.setPosition(x, y);
				level.addEntity(p);
				attackCooldown = Main.random.nextInt(isBlue ? 150 : 200) + 50;
			}
		} else {
			attackCooldown--;
		}
	}

	@Override
	public String getSprite() {
		if(isBlue) {
			return "enemy/bluefiresprite.png";
		} else {
			return "enemy/redfiresprite.png";
		}
	}
	@Override
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		baseY = y;
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(isBlue, "isBlue");
		sd.setObject(baseY, "baseY");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		isBlue = (boolean)sd.getObjectDefault("isBlue", false);
		baseY = (double)sd.getObjectDefault("baseY", y);
	}
}