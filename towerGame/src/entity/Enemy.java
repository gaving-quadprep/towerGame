package entity;

import java.awt.Graphics2D;

import main.Main;
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
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(this.attackDamage, "attackDamage");
		sd.setObject(this.attackCooldown, "attackCooldown");
		sd.setObject(this.isAttacking, "isAttacking");
		sd.setObject(this.facing, "facing");
		return sd;
	}
	public void render(Graphics2D g2) {
		int[] positions = this.getPositionOnScreen();
		if(this.facing==Direction.LEFT) {
			g2.drawImage(this.sprite, positions[0], positions[1], -Main.tileSize, Main.tileSize, null);
		} else {
			g2.drawImage(this.sprite, positions[0], positions[1], Main.tileSize, Main.tileSize, null);
		}
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.attackDamage = (double)sd.getObjectDefault("attackDamage",1);
		this.attackCooldown = (int)sd.getObjectDefault("attackCooldown",0);
		this.isAttacking = (boolean)sd.getObjectDefault("isAttacking",false);
		this.facing = (Direction)sd.getObjectDefault("facing",Direction.RIGHT);
	}

}
