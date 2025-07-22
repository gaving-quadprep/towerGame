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
	
	public void loadSprites() {
		super.loadSprites();
		this.headSprite = level.getSprite("enemy/sentinel_head.png");
		this.bodySprite = level.getSprite("enemy/sentinel_body.png");
		this.armSprite = level.getSprite("enemy/sentinel_arm.png");
	}
	
	public void render(WorldRenderer wr) {
		wr.drawImage(headSprite, x, y);
	}

}
