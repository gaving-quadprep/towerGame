package weapon;

import java.math.BigDecimal;

import map.Level;

public class Spell {
	public double manaCost;
	public void use(Level level) {
		
	}
	
	public static Spell healingSpell = new Spell(){
		@Override public void use(Level level) {
			level.player.health = level.player.health.add(BigDecimal.valueOf(5));
			level.player.health = level.player.health.min(BigDecimal.TEN);
		}
	};
}
