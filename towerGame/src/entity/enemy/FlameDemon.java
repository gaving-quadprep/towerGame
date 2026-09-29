package entity.enemy;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import main.Main;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;
import util.Direction;

public class FlameDemon extends Enemy {
	BufferedImage attackSprite;
	private boolean onGroundPrev = false;
	int attackSpread = 0;
	private static final Rectangle attackHitbox = new Rectangle(14, 30, 2, 2);

	public FlameDemon(Level level) {
		super(level);
		hitbox = new Rectangle(0, 0, 32, 32);
		attackDamage = 7.5D;
		attackCooldown = 150;
		maxHealth = BigDecimal.valueOf(25.0D);
		health = maxHealth;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadSprites() {
		super.loadSprites();
		attackSprite = level.getSprite("flamedemonattack.png");
	}
	@Override
	public void render(WorldRenderer wr) {
		if(facing == Direction.LEFT) {
			wr.drawTiledImage(sprite, x, y, 2, 2, isAttacking?32:16, 0, isAttacking?16:0, 16);
		} else {
			wr.drawTiledImage(sprite, x, y, 2, 2, isAttacking?16:0, 0, isAttacking?32:16, 16);
		}
		if(attackSpread > 0) {
			wr.drawImage(attackSprite, x-(((double)attackSpread/10)-1), y+(29D/16), (6D/16), (3D/16));
			wr.drawImage(attackSprite, x+(((double)attackSpread/10)+1), y+(29D/16), -(6D/16), (3D/16));
		}
	}
	@Override
	public String getSprite() {
		return "enemy/flamedemon.png";
	}
	@Override
	public void update() {
		onGroundPrev = onGround;
		super.update();
		if(CollisionChecker.checkHitboxes(level.player.hitbox, attackHitbox,
				level.player.x, level.player.y, x+(double)attackSpread/10, y) ||
				CollisionChecker.checkHitboxes(level.player.hitbox, attackHitbox,
						level.player.x, level.player.y, x-(double)attackSpread/10, y)) {
			doDamageTo(level.player, 2);
			level.player.damageTimer = 24;
		}
		if(xVelocity >= 0) {
			facing = Direction.RIGHT;
		} else {
			facing = Direction.LEFT;
		}
		if(attackCooldown == 0 && onGround && Math.abs(x-level.player.x) < 14 ) {
			attackCooldown = 160 + Main.random.nextInt(21);
			isAttacking = true;
			double angle=Math.atan2((level.player.x)-x, level.player.y-y);
			xVelocity= Math.sin(angle) / 13;
			yVelocity = -0.17;
			onGround = false;
		}
		if(onGround && !onGroundPrev) {
			if(isAttacking) {
				isAttacking = false;
				attackSpread = 1;
			}
			yVelocity = 0;
		}
		attackCooldown--;
		if(attackCooldown < 0) {
			attackCooldown = 0;
		}
		if(attackSpread > 0) {
			attackSpread++;
		}
		if(attackSpread > 40) {
			attackSpread = 0;
		}
	}
	@Override
	public int getSpriteWidth() {
		return 32;
	}
	@Override
	public String getDebugString() {
		return "attackSpread: " + attackSpread + "\nonGroundPrev: " + onGroundPrev;
	}
	//backwards compatability
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(((double)attackSpread) / 10, "attackSpread");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		attackSpread = (int)((double)sd.getObjectDefault("attackSpread", 0d) * 10);
	}

}
