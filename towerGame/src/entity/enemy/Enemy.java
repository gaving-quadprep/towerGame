package entity.enemy;

import java.math.BigDecimal;

import entity.DamageSource;
import entity.LivingEntity;
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
	public Enemy(Level level) {
		super(level);
		this.damageCooldown = 4;
		this.attackDamage = 1.0D;
	}
	public void update() {
		super.update();
		if(this.level.player!=null) {
			if(CollisionChecker.checkEntities(this,this.level.player)) {
				doDamageTo(level.player, this.attackDamage);
			}
		}
		//if(this.attackCooldown > 0 && this.shouldDecreaseAttackCooldown())
		//	this.attackCooldown--;
	}
	public void render(WorldRenderer wr) {
		if(this.facing == Direction.LEFT) {
			wr.drawImage(this.sprite, this.x, this.y, -1, 1);
		} else {
			wr.drawImage(this.sprite, this.x, this.y, 1, 1);
		}
	}
	
	@Override
	public void damage(double damage, DamageSource source) {
		super.damage(damage, source);
		if(this.markedForRemoval)
			if(level.player.mana.compareTo(BigDecimal.valueOf(15)) < 0)
				level.player.mana = level.player.mana.add(Main.ONE_TENTH);
	}
	public int getAttackCooldown() {
		return 60;
	}
	public boolean shouldDecreaseAttackCooldown() {
		return true;
	}
	public String getDebugString() {
		return "attackCooldown: " + this.attackCooldown + "\nisAttacking: " + this.isAttacking;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.attackDamage, "attackDamage");
		sd.setObject(this.attackCooldown, "attackCooldown");
		sd.setObject(this.isAttacking, "isAttacking");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.attackDamage = (double)sd.getObjectDefault("attackDamage",1);
		this.attackCooldown = (int)sd.getObjectDefault("attackCooldown",0);
		this.isAttacking = (boolean)sd.getObjectDefault("isAttacking",false);
	}
}
