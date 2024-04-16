package entity;

import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import util.CollisionChecker;

public class PuddleMonster extends Enemy {
	private int timeLeftBeforeAttacking;
	public PuddleMonster(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}
	public void update() {
		super.update();
		if(!this.isAttacking && this.timeLeftBeforeAttacking == 0) {
			if(CollisionChecker.distance(this, level.player) < 2) {
				this.timeLeftBeforeAttacking = 6;
			}
		}
		if(this.timeLeftBeforeAttacking > 0) {
			this.timeLeftBeforeAttacking--;
			if(this.timeLeftBeforeAttacking == 0)
				this.isAttacking = true;
		}
		this.attackDamage = this.isAttacking ? 1.5 : 0;
		this.shouldRenderHealthBar = this.isAttacking;
	}
	public String getSprite() {
		return "enemy/puddle.png";
	}
	public void render(WorldRenderer wr) {
		wr.drawTiledImage(this.sprite, this.x, this.y, 1, 1, this.isAttacking?32:this.timeLeftBeforeAttacking==0?0:16, 0, this.isAttacking?48:this.timeLeftBeforeAttacking==0?16:32, 16);
	}
	public void damage(double damage) {
		super.damage(this.isAttacking ? damage : damage/2);
		this.timeLeftBeforeAttacking = 60;
		this.isAttacking = false;
	}
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(timeLeftBeforeAttacking, "timeLeftBeforeAttacking");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		this.timeLeftBeforeAttacking = (int) sd.getObjectDefault("timeLeftBeforeAttacking", 0);
	}
}
