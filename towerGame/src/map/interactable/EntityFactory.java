package map.interactable;

import java.awt.Rectangle;

import javax.swing.JOptionPane;

import entity.Bomb;
import entity.Entity;
import entity.FallingTile;
import entity.ManaOrb;
import entity.enemy.FireEnemy;
import entity.enemy.FlameDemon;
import entity.enemy.PuddleMonster;
import entity.enemy.Thing;
import entity.enemy.ZombieKnight;
import levelEditor.LevelEditor;
import main.Main;
import map.Level;
import map.Tile;
import save.SerializedData;

public class EntityFactory extends TileWithData {
	public static class CustomTileData extends TileData {
		public CustomTileData(Entity entity) {
			this.entity = entity;
		}
		public CustomTileData() {
			this(null);
		}
		public void setDelay(int delay) {
			this.delay = delay;
		}
		Entity entity;
		int delay = 600;
		@Override
		public SerializedData serialize() {
			SerializedData sd = super.serialize();
			sd.setObject(entity == null ? null : entity.serialize(), "entity");
			sd.setObject(delay, "delay");
			return sd;
		}

		@Override
		public void deserialize(SerializedData sd) {
			if(sd.getObjectDefault("entity", null) != null) {
				SerializedData entity = (SerializedData) sd.getObjectDefault("entity", null);
				this.entity = Entity.entityRegistry.createByName((String) entity.getObjectDefault("class", null), new Class[] {Level.class}, new Object[] {null});
				this.entity.deserialize(entity);
				this.delay = (int)sd.getObjectDefault("delay", 600);
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
		if(Main.frames % ((CustomTileData)level.getTileData(x, y, foreground)).delay == 0) {
			Entity e = (Entity) ((CustomTileData)level.getTileData(x, y, foreground)).entity.clone();
			e.setPosition(x, y);
			level.addEntity(e);
		}
	}
	
	public TileData promptTileData() {
		String[] possibleValues = new String[] {"Fire Enemy", "Blue Fire Enemy", "Thing", "Puddle Monster", "Flame Demon", "Zombie Knight", "Falling Boulder", "Mana Orb", "Bomb"};
		
		String result = (String) JOptionPane.showInputDialog(null,
					 "Choose an entity", "Entity spawned",
					 JOptionPane.INFORMATION_MESSAGE, null,
					 possibleValues, possibleValues[2]);
		Entity e;
		if(result == null) {
			return null;
		}
		Level level = LevelEditor.gamePanel.level;
		switch(result) {
		case "Fire Enemy":
		case "Blue Fire Enemy":
			e = new FireEnemy(level, result.equals("Blue Fire Enemy"));
			break;
		case "Thing":
			e = new Thing(level);
			break;
		case "Puddle Monster":
			e = new PuddleMonster(level);
			break;
		case "Flame Demon":
			e = new FlameDemon(level);
			break;
		case "Zombie Knight":
			e = new ZombieKnight(level);
			break;
		case "Falling Boulder":
			e = new FallingTile(level, Tile.boulder.id);
			break;
		case "Mana Orb":
			e = new ManaOrb(level);
			break;
		case "Bomb":
			e = new Bomb(level);
			break;
		default:
			return null;
		}
		CustomTileData ret = new EntityFactory.CustomTileData(e);

		String userInput = JOptionPane.showInputDialog(null, "Delay between spawning entities (in seconds)", "Delay", JOptionPane.QUESTION_MESSAGE);
		if(userInput!=null) {
			ret.setDelay(Math.max((int)(60.0d * Double.parseDouble(userInput)), 1));
		}
		return ret;
	}
}
