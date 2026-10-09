package entity.enemy;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import map.Level;

public class BlazingShadow extends Enemy {
	public static enum State {
		WAITING_FOR_PLAYER,
		
	}
	private BufferedImage head, torso, leftArm, rightArm, leftClaw, rightClaw, bottom;
	public BlazingShadow(Level level) {
		super(level);
		health = maxHealth = BigDecimal.valueOf(125);
		attackDamage = 5;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadSprites() {
		head = level.getSprite("enemy/blazingshadow-head.png");
		torso = level.getSprite("enemy/blazingshadow-torso.png");
		leftArm = level.getSprite("enemy/blazingshadow-leftarm.png");
		rightArm = level.getSprite("enemy/blazingshadow-rightarm.png");
		leftClaw = level.getSprite("enemy/blazingshadow-leftclaw.png");
		rightClaw = level.getSprite("enemy/blazingshadow-rightclaw.png");
		bottom = level.getSprite("enemy/blazingshadow-bottom.png");
	}

}
