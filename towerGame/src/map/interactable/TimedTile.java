package map.interactable;

import entity.Entity;
import entity.LivingEntity;
import entity.TileDamageSource;
import main.Main;
import map.Level;
import save.SerializedData;
import util.Direction;
import util.TilePosition;

public class TimedTile extends TileWithData {
	
	public static class CustomTileData extends TileData {
		public boolean activated = false;
		public int timeLeft = 180;

		@Override
		public SerializedData serialize() {
			SerializedData sd = super.serialize();
			sd.setObject(activated, "activated");
			sd.setObject(timeLeft, "timeLeft");
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			this.activated = (boolean) sd.getObjectDefault("activated", false);
			this.timeLeft = (int) sd.getObjectDefault("timeLeft", 180);
		}
		
	}
	
	public TimedTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		this.defaultTileData = new CustomTileData();
		// TODO Auto-generated constructor stub
	}
	
	public TimedTile(int id, int textureId, boolean isSolid) {
		super(id, textureId, isSolid);
		this.defaultTileData = new CustomTileData();
		// TODO Auto-generated constructor stub
	}
	
	public int getTextureId(Level level, boolean foreground, int x, int y) {
		if(level!=null) {
			CustomTileData td = (CustomTileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
			if(td.activated)
				if(td.timeLeft == 0) {
					return this.textureId + 4 + (Main.frames / 2) % 3;
				} else {
					return (td.timeLeft / 30) % 2 == 0 ? this.textureId : this.textureId + 1 + (td.timeLeft / 60);
				}
			return this.textureId;
		}
		return this.textureId;
	}
	
	public void update(Level level, int x, int y, boolean foreground) {
		CustomTileData td = (CustomTileData) (foreground ? level.getTileDataForeground(x, y) : level.getTileDataBackground(x, y));
		
		if (td.activated)
			if(td.timeLeft > 0)
				td.timeLeft--;
	}
	
	public void onTouch(Level level, Entity entity, Direction direction, int x, int y) {
		CustomTileData td = (CustomTileData) (level.getTileDataForeground(x, y));
		if(!td.activated) {
			td.activated = true;
		} else {
			if (td.timeLeft == 0)
				if (entity instanceof LivingEntity)
					((LivingEntity)entity).damage(500, new TileDamageSource(new TilePosition(x, y)));
		}
	}
}
