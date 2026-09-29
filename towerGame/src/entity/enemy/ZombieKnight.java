package entity.enemy;

import java.awt.Rectangle;
import java.math.BigDecimal;

import entity.DroppedItem;
import entity.Entity;
import item.ItemWeapon;
import main.Main;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;
import weapon.Weapon;

public class ZombieKnight extends FollowingEnemy {
	private final Rectangle regularHitbox = CollisionChecker.getHitbox(4, 1, 12, 16);
	private final Rectangle attackHitbox = CollisionChecker.getHitbox(0, 1, 16, 16);
	Entity target;
	public ZombieKnight(Level level) {
		super(level);
		hitbox = regularHitbox;
		attackDamage = 3D;
		attackCooldown = 0;
		maxHealth = BigDecimal.valueOf(10.0D);
		health = maxHealth;
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getSprite() {
		return "enemy/zombieknight.png";
	}
	@Override
	public void update() {
		super.update();
		if(isAttacking)
			if(CollisionChecker.checkHitboxes(attackHitbox, level.player.hitbox, x, y, level.player.x, level.player.y))
				doDamageTo(level.player, attackDamage);
	}
	@Override
	public void render(WorldRenderer wr) {
		if(facing==Direction.LEFT) {
			wr.drawTiledImage(sprite, x - 0.5, y, 1.5, 1, 24, isAttacking?16:0, 0, isAttacking?32:16);
		} else {
			wr.drawTiledImage(sprite, x, y, 1.5, 1, 0, isAttacking?16:0, 24, isAttacking?32:16);
		}
	}
	@Override
	public String getDebugString() {
		return "target:" + target + "\ncanGoToPlayer: " + canGoTo((int)Math.round(level.player.x), (int)Math.round(level.player.y));
	}
	@Override
	public void onDied() {
		super.onDied();
		if(Main.random.nextInt(20) == 1) {
			Entity e = new DroppedItem(level, new ItemWeapon(Weapon.sword.id));
			e.setPosition(x, y);
			level.addEntity(e);
		}
	}

}
