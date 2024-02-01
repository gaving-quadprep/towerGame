package map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Serializable;

import main.Main;

public class CustomTile extends Tile implements Serializable {
	private static final long serialVersionUID = -1294160863068675269L;
	BufferedImage texture;
	public CustomTile(int id, BufferedImage texture, boolean isSolid) {
		super(id, -1, isSolid);
		assert id > 255 : "Custom Tile IDs must be > 255";
		this.texture=texture;
	}
	public void render(Level level, Graphics2D g2, int posX, int posY, boolean foreground) {
		if(!foreground) {
			g2.drawImage(level.tilemap_dark, posX*Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize-(int)(level.cameraY*Main.tileSize), 16, 16, (ImageObserver)null);
		}else {
			g2.drawImage(level.tilemap, posX*Main.tileSize-(int)(level.cameraX*Main.tileSize), posY*Main.tileSize-(int)(level.cameraY*Main.tileSize), 16, 16, (ImageObserver)null);
		}
	}

}
