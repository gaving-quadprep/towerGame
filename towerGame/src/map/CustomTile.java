package map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import entity.Entity;
import entity.LivingEntity;
import main.Direction;
import main.Main;
import towerGame.Player;

public class CustomTile extends Tile {
	public BufferedImage texture;
	BufferedImage texture_dark;
	public boolean doesDamage;
	public CustomTile(int id, BufferedImage texture, boolean isSolid, boolean doesDamage) {
		super(id, -1, isSolid);
		customTiles[this.id-256]=this;
		assert id > 255 : "Custom Tile IDs must be > 255";
		this.texture=texture;
		this.doesDamage=doesDamage;
	}
	public CustomTile(int id, BufferedImage texture, boolean isSolid, boolean doesDamage, Rectangle hitbox) {
		super(id, -1, isSolid, hitbox);
		customTiles[this.id-256]=this;
		assert id > 255 : "Custom Tile IDs must be > 255";
		this.texture=texture;
		this.doesDamage=doesDamage;
	}
	public CustomTile(BufferedImage texture, boolean isSolid, boolean doesDamage) {
		this(Tile.nextCustomTileId++, texture, isSolid, doesDamage);
	}
	public CustomTile(BufferedImage texture, boolean isSolid, boolean doesDamage, Rectangle hitbox) {
		this(Tile.nextCustomTileId++, texture, isSolid, doesDamage, hitbox);
	}
	public void render(Level level, Graphics2D g2, int posX, int posY, boolean foreground) {
		if(this.texture_dark==null) {
			this.texture_dark = level.bg_tint.filter(texture, null);
		}
		if(!foreground) {
			g2.drawImage(this.texture_dark, posX*Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize-(int)(level.cameraY*Main.tileSize), Main.tileSize, Main.tileSize, (ImageObserver)null);
		}else {
			g2.drawImage(this.texture, posX*Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize-(int)(level.cameraY*Main.tileSize), Main.tileSize, Main.tileSize, (ImageObserver)null);
		}
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		super.onTouch(level, entity, direction, x, y);
		if(this.doesDamage) {
			if(entity instanceof LivingEntity) {
				((LivingEntity)entity).damage(1);
			}
			if(entity instanceof Player) {
				((Player)entity).health = 0;
			}
		}
	}

}
