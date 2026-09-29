package entity.enemy;

import java.awt.image.BufferedImage;

import main.WorldRenderer;
import map.Level;

public class Sentinel extends Enemy {
	BufferedImage headSprite;
	BufferedImage bodySprite;
	BufferedImage armSprite;

	public Sentinel(Level level) {
		super(level);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadSprites() {
		super.loadSprites();
		headSprite = level.getSprite("enemy/sentinel_head.png");
		bodySprite = level.getSprite("enemy/sentinel_body.png");
		armSprite = level.getSprite("enemy/sentinel_arm.png");
	}

	@Override
	public void render(WorldRenderer wr) {
		wr.drawImage(headSprite, x, y);
	}

}
