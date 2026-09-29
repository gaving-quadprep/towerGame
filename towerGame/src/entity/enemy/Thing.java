package entity.enemy;

import java.math.BigDecimal;

import main.Main;
import main.WorldRenderer;
import map.Level;
import util.CollisionChecker;
import util.Direction;

public class Thing extends Enemy {
	public Thing(Level level) {
		super(level);
		hitbox = CollisionChecker.getHitbox(2, 0, 14, 16);
		attackCooldown = 60;
		health = BigDecimal.valueOf(5);
		maxHealth = BigDecimal.valueOf(5);
		attackDamage = 5;
	}
	@Override
	public String getSprite() {
		return "enemy/thing.png";
	}
	@Override
	public void render(WorldRenderer wr) {
		wr.drawTiledImage(sprite, x, y, 1, 1, isAttacking?16:0, 0, isAttacking?32:16, 16);
	}
	@Override
	public void update() {
		super.update();
		if(xVelocity >= 0) {
			facing = Direction.RIGHT;
		}else {
			facing = Direction.LEFT;
		}
		if(attackCooldown == 0 && onGround && Math.hypot(Math.abs(x-level.player.x), Math.abs(y-level.player.y)) < 6) {
			attackCooldown = 170 + Main.random.nextInt(21);
			isAttacking = true;
			double angle=Math.atan2((level.player.x)-x, level.player.y-y);
			xVelocity=Math.sin(angle)/7.5;
			yVelocity=Math.cos(angle)/4.5-0.1 - (0.002 * Math.abs(level.player.x-x));
			onGround = false;
		}
		if(onGround) {
			isAttacking = false;
		}
		attackCooldown--;
		if( attackCooldown < 0) {
			attackCooldown = 0;
		}
	}
}
