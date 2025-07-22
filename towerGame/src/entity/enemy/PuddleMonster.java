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
		this.hitbox = CollisionChecker.getHitbox(3, 13, 14, 15);
		// TODO Auto-generated constructor stub
	}
	public void update() {
		super.update();
		//if(!this.isAttacking && this.timeLeftBeforeAttacking == 0) {
			if(CollisionChecker.distance(this, level.player) < 2.2) {
				if(this.state == State.WAITING) {
					this.state = State.EMERGING;
					this.hitbox = CollisionChecker.getHitbox(3, 2, 14, 15);
					this.timer = 10;
				}
			}else {
				if(this.state == State.ATTACKING) {
					if(CollisionChecker.distance(this, level.player) > 5.5) {
						this.isAttacking = false;
						this.state = State.RETREATING;
						this.timer = 10;
					}
				}
			}
		//}
		if(this.timer > 0) {
			this.timer--;
			if(this.timer == 0)
				this.state = State.fromNumber((this.state.i + 1)% 4);
				if(this.state == State.WAITING) {
					this.hitbox = CollisionChecker.getHitbox(3, 13, 14, 15);
				} else {
					this.hitbox = CollisionChecker.getHitbox(3, 2, 14, 15);
				}
		}
		this.isAttacking = this.state == State.ATTACKING;
		this.attackDamage = this.isAttacking ? 1.5 : 0;
		this.shouldRenderHealthBar = this.isAttacking;
	}
	public String getSprite() {
		return "enemy/puddle.png";
	}
	public void render(WorldRenderer wr) {
		switch(this.state) {
		case WAITING:
			wr.drawTiledImage(this.sprite, this.x, this.y, 1, 1, 0, 0, 16, 16);
			break;
		case EMERGING:
		case RETREATING:
			wr.drawTiledImage(this.sprite, this.x, this.y, 1, 1, 16, 0, 32, 16);
			break;
		case ATTACKING:
			wr.drawTiledImage(this.sprite, this.x, this.y, 1, 1, 32, 0, 48, 16);
			break;
		}
	}
	public void damage(double damage, DamageSource source) {
		super.damage(this.state == State.WAITING ? damage/4 : this.state == State.RETREATING ? damage/2 : damage, source);
		if(this.state != State.WAITING) {
			this.timer = 60;
			this.state = State.RETREATING;
			this.isAttacking = false;
		}
	}
	public String getDebugString() {
		return "state: " + this.state + "\ntimer: " + this.timer;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(timer, "timeLeftBeforeAttacking");
		sd.setObject(state.i, "state");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.timer = (int) sd.getObjectDefault("timeLeftBeforeAttacking", 0);
		this.state = State.fromNumber((int) sd.getObjectDefault("state", State.WAITING.i));
	}
}
