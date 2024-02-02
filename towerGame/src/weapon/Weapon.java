package weapon;

import entity.Entity;
import entity.LivingEntity;
import main.CollisionChecker;
import main.Direction;
import map.Level;
import towerGame.Player;

public class Weapon {
	public int id;
	public String texture;
	public double damage;
	public static final Weapon[] weapons=new Weapon[256];
	public void onAttack(Level level, Player player, boolean isMouseRight, int mouseX, int mouseY) {
		for(Entity e : level.entities) {
			if(e instanceof LivingEntity){
				if(CollisionChecker.checkHitboxes(player.hitbox,e.hitbox,player.x+(player.facing == Direction.LEFT ? -0.5f: 0.5f),player.y,e.x,e.y)) {
					((LivingEntity) e).damage(this.damage);
				}
			}
		}
	}
	public void onMouseHeld(Level level, Player player, int mouseX, int mouseY) {};
	public Weapon(int id, String texture, double damage) {
		this.id=id;
		this.texture=texture;
		this.damage=damage;
		weapons[id] = this;
	}
	public static final Weapon sword = new Weapon(0, "sword.png",1.0f);
	public static final Weapon staff = new Staff(1, "staff.png",1);
	public static final Weapon staffUpgraded = new Staff(2, "staff2.png",2);
	public static final Weapon staffUpgraded2 = new Staff(3, "staff3.png",3);
	public static final Weapon shield = new Shield(4, "shield.png");
	public static final Weapon dagger = new Weapon(5, "dagger.png", 1.5f);
}
