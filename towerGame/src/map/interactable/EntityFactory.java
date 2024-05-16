package map.interactable;

import java.awt.Rectangle;

import entity.Entity;
import entity.Thing;
import gui.TileInteractionGUI;
import main.Main;
import map.Level;
import map.Tile;
import save.SerializedData;
import towerGame.TowerGame;

public class EntityFactory extends TileWithData {
	public static class CustomTileData extends TileData {
		public CustomTileData(Entity entity) {
			this.entity = entity;
		}
		public CustomTileData() {
			this(null);
		}
		
		Entity entity;
		@Override
		public SerializedData serialize() {
			SerializedData sd = super.serialize();
			sd.setObject(entity == null ? null : entity.serialize(), "entity");
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			if(sd.getObjectDefault("entity", null) != null) {
				SerializedData entity = (SerializedData) sd.getObjectDefault("entity", null);
				this.entity = Entity.entityRegistry.createByName((String) entity.getObjectDefault("class", null), new Class[] {Level.class}, new Object[] {null});
				this.entity.deserialize(entity);
			}
		}

	}

	public EntityFactory(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		defaultTileData = new CustomTileData(new Thing(null));
		// TODO Auto-generated constructor stub
	}

	public EntityFactory(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		defaultTileData = new CustomTileData(new Thing(null));
		// TODO Auto-generated constructor stub
	}
	public void update(Level level, int x, int y, boolean foreground) {
		super.update(level, x, y, foreground);
		if(foreground) {
			if(Main.frames % 600 == 0) {
				Entity e = (Entity) ((CustomTileData)level.getTileData(x, y, foreground)).entity.clone();
				e.setPosition(x, y);
				level.addEntity(e);
			}
		}
	}
}
