package weapon;

import java.math.BigDecimal;

import map.Level;

public class Spell {
	public double manaCost;
	public void use(Level level) {
		level.player.mana = level.player.mana.subtract(BigDecimal.valueOf(manaCost));
	}
	public static Spell healingSpell = new Spell() {
		@Override public void use(Level level) {
			super.use(level);
			level.player.health = level.player.health.add(BigDecimal.valueOf(5));
			level.player.health = level.player.health.min(BigDecimal.TEN);
		}
	};
}
