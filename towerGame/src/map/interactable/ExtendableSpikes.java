package map.interactable;

import java.awt.Rectangle;
import java.math.BigDecimal;

import entity.Entity;
import entity.LivingEntity;
import entity.TileDamageSource;
import main.Main;
import map.Level;
import save.SerializedData;
import towerGame.Player;
import util.Direction;
import util.TilePosition;

public class ExtendableSpikes extends TileWithData {
	public static class CustomTileData extends TileData {
		public boolean extended;
		public boolean extending;
		public int extendingStage;
		
		public SerializedData serialize() {
			SerializedData sd = super.serialize();
			sd.setObject(extended, "extended");
			sd.setObject(extending, "extending");
			sd.setObject(extendingStage, "extendingStage");
			return sd;
		}

		public void deserialize(SerializedData sd) {
			this.extended = (Boolean) sd.getObjectDefault("extended", false);
			this.extending = (Boolean) sd.getObjectDefault("extending", false);
			this.extendingStage = (Integer) sd.getObjectDefault("extendingStage", 0);
		}
	}
	
	public ExtendableSpikes(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		this.defaultTileData = new CustomTileData();
		// TODO Auto-generated constructor stub
	}
	
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(level!=null) {
			CustomTileData td = (CustomTileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
			return td.extended ? this.textureId+3 : td.extending ? this.textureId+td.extendingStage : this.textureId;
		}
		return this.textureId;
	}
	
	public void update(Level level, int x, int y, boolean foreground) {
		CustomTileData td = (CustomTileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
		
		if(td.extending && Main.frames%4 == 0) {
			td.extendingStage++;
			if(td.extendingStage==3) {
				td.extending = false;
				td.extended = true;
			}
		}
	}
	
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		CustomTileData td = (CustomTileData) (level.getTileDataForeground(x, y));
		if(td.extended) {
			super.onTouch(level, entity, direction, x, y);
			if(entity instanceof LivingEntity) {
				((LivingEntity)entity).damage(1, new TileDamageSource(new TilePosition(x, y)));
			}
			if(entity instanceof Player) {
				((Player)entity).health = BigDecimal.ZERO;
			}
		}
	}
	
}
