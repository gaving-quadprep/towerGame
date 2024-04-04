package map.interactable;

import java.awt.Rectangle;

import entity.DroppedItem;
import entity.Entity;
import gui.TileInteractionGUI;
import item.Item;
import map.Level;
import map.Tile;
import map.interactable.BlockedExit.CustomTileData;
import save.SerializedData;
import towerGame.TowerGame;

public class ChestTile extends TileWithData {
	public static class CustomTileData extends TileData {
		public CustomTileData(Item item) {
			this.item = item;
		}
		public CustomTileData() {
			this(null);
		}
		
		Item item;
		@Override
		public SerializedData serialize() {
			SerializedData sd = super.serialize();
			sd.setObject(item == null ? null : item.serialize(), "item");
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			if(sd.getObjectDefault("item", null) != null) {
				SerializedData item = (SerializedData) sd.getObjectDefault("item", null);
				this.item = Item.itemRegistry.createByName((String) item.getObjectDefault("class", "Item"), null, null);
				this.item.deserialize(item);
			}
		}

	}

	public ChestTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		defaultTileData = new CustomTileData(null);
		// TODO Auto-generated constructor stub
	}

	public ChestTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		defaultTileData = new CustomTileData(null);
		// TODO Auto-generated constructor stub
	}
	public void onDestroyed(Level level, int x, int y) {
		Entity droppedItem = new DroppedItem(level, ((CustomTileData)level.getTileDataForeground(x, y)).item);
		droppedItem.setPosition(x, y);
		level.addEntity(droppedItem);
	}
	public void onApproachedByPlayer(Level level, int x, int y) {
		if(this.id != Tile.crate.id)
			TowerGame.showUnique(new TileInteractionGUI());
	}

}
