package entity;

import java.math.BigDecimal;

import main.Main;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class Enemy extends LivingEntity {
	public boolean isAttacking = false;
	public double attackDamage;
	public int attackCooldown;
	public Direction facing = Direction.RIGHT;
	public Enemy(Level level) {
		super(level);
		this.damageCooldown = 4;
		this.attackDamage = 1.0D;
	}
	public void update() {
		super.update();
		if(this.level.player!=null) {
			if(CollisionChecker.checkEntities(this,this.level.player)) {
				this.level.player.damage(this.attackDamage);
			}
		}
	}
	public void render(WorldRenderer wr) {
		if(this.facing==Direction.LEFT) {
			wr.drawImage(this.sprite, this.x, this.y, -1, 1);
		} else {
			wr.drawImage(this.sprite, this.x, this.y, 1, 1);
		}
	}
	public void damage(double damage) {
		super.damage(damage);
		if(this.markedForRemoval)
			if(level.player.mana.compareTo(BigDecimal.valueOf(15)) < 0)
				level.player.mana = level.player.mana.add(Main.ONE_TENTH);
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.attackDamage, "attackDamage");
		sd.setObject(this.attackCooldown, "attackCooldown");
		sd.setObject(this.isAttacking, "isAttacking");
		sd.setObject(this.facing, "facing");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.attackDamage = (double)sd.getObjectDefault("attackDamage",1);
		this.attackCooldown = (int)sd.getObjectDefault("attackCooldown",0);
		this.isAttacking = (boolean)sd.getObjectDefault("isAttacking",false);
		this.facing = (Direction)sd.getObjectDefault("facing",Direction.RIGHT);
	}

}
