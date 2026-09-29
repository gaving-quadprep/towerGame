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
		damageCooldown = 4;
		attackDamage = 1.0D;
	}
	@Override
	public void update() {
		super.update();
		if((level.player!=null) && CollisionChecker.checkEntities(this,level.player)) {
			doDamageTo(level.player, attackDamage);
		}
		//if(this.attackCooldown > 0 && this.shouldDecreaseAttackCooldown())
		//	this.attackCooldown--;
	}
	@Override
	public void render(WorldRenderer wr) {
		if(facing == Direction.LEFT) {
			wr.drawImage(sprite, x, y, -1, 1);
		} else {
			wr.drawImage(sprite, x, y, 1, 1);
		}
	}

	@Override
	public void damage(double damage, DamageSource source) {
		super.damage(damage, source);
		if(markedForRemoval)
			if(level.player.mana.compareTo(BigDecimal.valueOf(15)) < 0)
				level.player.mana = level.player.mana.add(Main.ONE_TENTH);
	}
	public int getAttackCooldown() {
		return 60;
	}
	public boolean shouldDecreaseAttackCooldown() {
		return true;
	}
	@Override
	public String getDebugString() {
		return "attackCooldown: " + attackCooldown + "\nisAttacking: " + isAttacking;
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(attackDamage, "attackDamage");
		sd.setObject(attackCooldown, "attackCooldown");
		sd.setObject(isAttacking, "isAttacking");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		attackDamage = (double)sd.getObjectDefault("attackDamage",1);
		attackCooldown = (int)sd.getObjectDefault("attackCooldown",0);
		isAttacking = (boolean)sd.getObjectDefault("isAttacking",false);
	}
}
