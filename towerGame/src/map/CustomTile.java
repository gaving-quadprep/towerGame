package map;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

import entity.Entity;
import entity.LivingEntity;
import main.WorldRenderer;
import save.ISerializable;
import save.SerializedData;
import towerGame.Player;
import util.Direction;

public class CustomTile extends Tile implements ISerializable {
	public BufferedImage texture;
	BufferedImage texture_dark;
	public boolean doesDamage;
	public String name;
	public CustomTile(int id, BufferedImage texture, boolean isSolid, boolean doesDamage) {
		super(id, -1, isSolid);
		customTiles[this.id-4096]=this;
		assert id > 4095 : "Custom Tile IDs must be > 4095";
		this.texture=texture;
		this.doesDamage=doesDamage;
	}
	public CustomTile(int id, BufferedImage texture, boolean isSolid, boolean doesDamage, Rectangle hitbox) {
		super(id, -1, isSolid, hitbox);
		customTiles[this.id-4096]=this;
		assert id > 4095 : "Custom Tile IDs must be > 4095";
		this.texture=texture;
		this.doesDamage=doesDamage;
	}
	public CustomTile(BufferedImage texture, boolean isSolid, boolean doesDamage) {
		this(Tile.nextCustomTileId++, texture, isSolid, doesDamage);
	}
	public CustomTile(BufferedImage texture, boolean isSolid, boolean doesDamage, Rectangle hitbox) {
		this(Tile.nextCustomTileId++, texture, isSolid, doesDamage, hitbox);
	}
	public void render(Level level, WorldRenderer wr, int x, int y, boolean foreground) {
		if(this.texture_dark==null) {
			this.texture_dark = level.bg_tint.filter(texture, null);
		}
		if(!foreground) {
			wr.drawImage(this.texture_dark, x, y, 1, 1);
		}else {
			wr.drawImage(this.texture, x, y, 1, 1);
		}
	}
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		super.onTouch(level, entity, direction, x, y);
		if(this.doesDamage) {
			if(entity instanceof LivingEntity) {
				((LivingEntity)entity).damage(1);
			}
			if(entity instanceof Player) {
				((Player)entity).health = BigDecimal.ZERO;
			}
		}
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(this.id-4096, "id");
		if(this.hasCustomHitbox)
			sd.setObject(this.hitbox, "hitbox");
		sd.setObject(this.isSolid, "isSolid");
		sd.setObject(this.doesDamage, "doesDamage");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(this.texture, "png", stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sd.setObject(stream.toByteArray(), "texture");
		sd.setObject(this.name, "name");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		// TODO Auto-generated method stub
		
	}

}
