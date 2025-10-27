package entity.enemy;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import map.Level;

public class BlazingShadow extends Enemy {
	private BufferedImage head, torso, leftArm, rightArm, leftClaw, rightClaw, bottom;
	public BlazingShadow(Level level) {
		super(level);
		health = maxHealth = BigDecimal.valueOf(125);
		// TODO Auto-generated constructor stub
	}

}
