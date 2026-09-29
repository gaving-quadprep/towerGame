package entity;

import item.Item;
import main.WorldRenderer;
import map.Level;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;

public class DroppedItem extends GravityAffectedEntity {
	Item item;
	public DroppedItem(Level level, Item item) {
		super(level);
		this.item = item;
		hitbox = CollisionChecker.getHitbox(1, 1, 15, 15);
		if(this.item == null) {
			markedForRemoval = true;
		}
	}
	public DroppedItem(Level level) {
		this(level, null);
	}

	@Override
	public void update() {
		super.update();
		Player p = level.player;
		if((p != null) && CollisionChecker.checkEntities(this, p)) {
			if(p.addToInventory(item)) {
				markedForRemoval = true;
			}
		}
	}
	@Override
	public void render(WorldRenderer wr) {
		if(item != null) {
			wr.drawImage(sprite, x, y, 1, 1);
		}
	}
	@Override
	public String getSprite() {
		return item != null ? item.getSprite() : null;
	}

	@Override
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(item == null ? null : item.serialize(), "item");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		if(sd.getObjectDefault("item", null) != null) {
			SerializedData item = (SerializedData) sd.getObjectDefault("item", null);
			this.item = Item.itemRegistry.createByName((String) item.getObjectDefault("class", "Item"), null, null);
			this.item.deserialize(item);
		}
	}

}
