package entity;

import java.awt.Graphics2D;

import item.Item;
import main.Main;
import map.Level;
import save.SerializedData;
import towerGame.Player;
import util.CollisionChecker;

public class DroppedItem extends GravityAffectedEntity {
	Item item;
	public DroppedItem(Level level, Item item) {
		super(level);
		this.item = item;
		this.hitbox = CollisionChecker.getHitbox(1, 0, 15, 16);
		if(this.item == null) {
			this.markedForRemoval = true;
		}
	}
	public DroppedItem(Level level) {
		this(level, null);
	}

	public void update() {
		super.update();
		Player p = this.level.player;
		if(p != null) {
			if(CollisionChecker.checkEntities(this, p)) {
				if(p.addToInventory(this.item)) {
					this.markedForRemoval = true;
				}
			}
		}
	}
	public void render(Graphics2D g2) {
		if(this.item != null) {
			int[] positions = this.getPositionOnScreen();
			if((positions[0]+(16*Main.scale) > 0 && positions[0] < 320*Main.scale)&&(positions[1]+(16*Main.scale) > 0 && positions[1] < 240*Main.scale))
				g2.drawImage(this.sprite,positions[0],positions[1],Main.tileSize,Main.tileSize,null);
		}
	}
	public String getSprite() {
		return this.item != null ? this.item.getSprite() : "";
	}
	
	public SerializedData serialize() {
		SerializedData sd = super.serialize();
		sd.setObject(item == null ? null : item.serialize(), "item");
		return sd;
	}
	public void deserialize(SerializedData sd) {
		super.deserialize(sd);
		if(sd.getObjectDefault("item", null) != null) {
			SerializedData item = (SerializedData) sd.getObjectDefault("item", null);
			this.item = Item.itemRegistry.createByName((String) item.getObjectDefault("class", "Item"), null, null);
			this.item.deserialize(item);
		}
	}

}
