package map.interactable;

import java.awt.Rectangle;

import entity.DroppedItem;
import gui.TileInteractionGUI;
import item.Item;
import map.Level;
import map.Tile;
import map.interactable.BlockedExit.TileData;
import save.SerializedData;
import towerGame.TowerGame;

public class ChestTile extends TileWithData {
	public static class TileData extends BaseTileData {
		public TileData(Item item) {
			this.item = item;
		}
		
		Item item;
		@Override
		public SerializedData serialize() {
			SerializedData sd = new SerializedData();
			sd.setObject(item.serialize(), "item");
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			this.item = Item.itemRegistry.createByName((String) sd.getObjectDefault("class", "Item"), null, null);
			item.deserialize((SerializedData) sd.getObjectDefault("item", null));
		}

	}

	public ChestTile(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		defaultTileData = new TileData(null);
		// TODO Auto-generated constructor stub
	}

	public ChestTile(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		defaultTileData = new TileData(null);
		// TODO Auto-generated constructor stub
	}
	public void onDestroyed(Level level, int x, int y) {
		level.addEntity(new DroppedItem(level, ((TileData)level.tileDataForeground[x][y]).item));
	}
	public void onApproachedByPlayer(Level level, int x, int y) {
		if(this.id != Tile.crate.id)
			TowerGame.showUnique(new TileInteractionGUI());
	}

}
