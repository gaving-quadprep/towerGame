package entity;

import java.awt.Rectangle;

import main.CollisionChecker;
import main.Main;
import map.Level;

public class FireEnemy extends Enemy {
	private static final long serialVersionUID = -2179895653900277932L;
	public boolean isBlue;
	public double baseY;
	public FireEnemy(Level level, boolean isBlue) {
		super(level);
		this.attackCooldown=200;
		this.isBlue=isBlue;
		this.hitbox=new Rectangle(1,1,15,15);
		this.attackDamage = 1.0F;
		this.maxHealth=10.0f;
		this.health=this.maxHealth;
		if(this.isBlue) {
			this.attackDamage+=0.5F;
			this.maxHealth+=2.5F;
			this.health=this.maxHealth;
		}
	}
	public FireEnemy(Level level) {
		this(level,false);
	}
	public void update() {
		if(this.damageTimer!=0) {
			this.damageTimer--;
		}
		if(this.level.player!=null) {
			if(CollisionChecker.checkEntities(this,this.level.player)) {
				this.level.player.damage(this.attackDamage);
			}
		}
		this.y=baseY+(double) Math.sin(((double)Main.frames)/30.0D);
		if(this.attackCooldown==0) {
			double angle=(double)Math.atan2((this.level.player.x)-this.x, this.level.player.y-this.y);
			FireProjectile p = new FireProjectile(this.level, this.isBlue);
			p.xVelocity=(double) Math.sin(angle)/4.5F;
			p.yVelocity=(double) (Math.cos(angle)/4.5F)-0.1F - (0.002F * Math.abs(this.level.player.x-this.x));
			p.setPosition(this.x, this.y);
			this.level.addEntity(p);
			this.attackCooldown = (int)(Math.random() * (this.isBlue ? 150 : 200))+50;
		}else {
			this.attackCooldown--;
		}
	}

	public String getSprite() {
		if(this.isBlue) {
			return "bluefiresprite.png";
		} else {
			return "redfiresprite.png";
		}
	}
	public void setPosition(double x, double y) {
		super.setPosition(x, y);
		this.baseY=y;
	}
}