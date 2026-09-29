package entity.enemy;

import entity.DamageSource;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;

public class PuddleMonster extends Enemy {
	public static enum State {
		WAITING(0),
		EMERGING(1),
		ATTACKING(2),
		RETREATING(3);

		public final int i;
		State(int i) {
			this.i = i;
		}
		public static State fromNumber(int i) {
			switch(i) {
			case 0:
				return WAITING;
			case 1:
				return EMERGING;
			case 2:
				return ATTACKING;
			case 3:
			default:
				return RETREATING;
			}
		}
	}

	private int timer;
	public State state = State.WAITING;
	public PuddleMonster(Level level) {
		super(level);
		hitbox = CollisionChecker.getHitbox(3, 13, 14, 15);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void update() {
		super.update();
		//if(!this.isAttacking && this.timeLeftBeforeAttacking == 0) {
		if(CollisionChecker.distance(this, level.player) < 2.2) {
			if(state == State.WAITING) {
				state = State.EMERGING;
				hitbox = CollisionChecker.getHitbox(3, 2, 14, 15);
				timer = 10;
			}
		} else if((state == State.ATTACKING) && (CollisionChecker.distance(this, level.player) > 5.5)) {
			isAttacking = false;
			state = State.RETREATING;
			timer = 10;
		}
		//}
		if(timer > 0) {
			timer--;
			if(timer == 0)
				state = State.fromNumber((state.i + 1)% 4);
			if(state == State.WAITING) {
				hitbox = CollisionChecker.getHitbox(3, 13, 14, 15);
			} else {
				hitbox = CollisionChecker.getHitbox(3, 2, 14, 15);
			}
		}
		isAttacking = state == State.ATTACKING;
		attackDamage = isAttacking ? 1.5 : 0;
		shouldRenderHealthBar = isAttacking;
	}
	@Override
	public String getSprite() {
		return "enemy/puddle.png";
	}
	@Override
	public void render(WorldRenderer wr) {
		switch(state) {
		case WAITING:
			wr.drawTiledImage(sprite, x, y, 1, 1, 0, 0, 16, 16);
			break;
		case EMERGING:
		case RETREATING:
			wr.drawTiledImage(sprite, x, y, 1, 1, 16, 0, 32, 16);
			break;
		case ATTACKING:
			wr.drawTiledImage(sprite, x, y, 1, 1, 32, 0, 48, 16);
			break;
		}
	}
	@Override
	public void damage(double damage, DamageSource source) {
		super.damage(state == State.WAITING ? damage/4 : state == State.RETREATING ? damage/2 : damage, source);
		if(state != State.WAITING) {
			timer = 60;
			state = State.RETREATING;
			isAttacking = false;
		}
	}
	@Override
	public String getDebugString() {
		return "state: " + state + "\ntimer: " + timer;
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(timer, "timeLeftBeforeAttacking");
		sd.setObject(state.i, "state");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		timer = (int) sd.getObjectDefault("timeLeftBeforeAttacking", 0);
		state = State.fromNumber((int) sd.getObjectDefault("state", State.WAITING.i));
	}
}
