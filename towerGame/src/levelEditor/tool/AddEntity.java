package levelEditor.tool;

import java.awt.image.BufferedImage;

import entity.Bomb;
import entity.BombGuy;
import entity.Entity;
import entity.FireEnemy;
import entity.FlameDemon;
import entity.FloatingPlatform;
import entity.ManaOrb;
import entity.PuddleMonster;
import entity.Thing;
import entity.ZombieKnight;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import main.Main;
import main.WorldRenderer;
import map.Level;
import util.Position;

public class AddEntity extends Tool {
	@Override
	public void onMouseLeftClick(LevelEditor le) {
		Level level = le.level;
		Entity e;
		switch(le.drawEntity) {
		case 0:
			e = new FireEnemy(level, false);
			break;
		case 1:
			e = new FireEnemy(level, true);
			break;
		case 2:
			e = new Thing(level);
			break;
		case 3:
			e = new ManaOrb(level);
			break;
		case 4:
			e = new FloatingPlatform(level);
			break;
		case 5:
			e = new FlameDemon(level);
			break;
		case 6:
			e = new PuddleMonster(level);
			break;
		case 7:
			e = new ZombieKnight(level);
			break;
		case 8:
			e = new Bomb(level);
			break;
		case 9:
			e = new BombGuy(level);
			break;
		default:
			e = null;
		}
		if(le.eventHandler.shiftPressed) {
			Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
			e.setPosition(p.x - 0.5, p.y - 0.5);
		} else {
			int[] positions = LevelEditorUtils.getTilePosFromMouse();
			e.setPosition(positions[0], positions[1]);
		}
		level.addEntity(e);
	}
	
	@Override
	public void render(LevelEditor le, WorldRenderer wr) {
		BufferedImage entitysprite;
		int sizeX = 1, sizeY = 1;
		Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
		switch(le.drawEntity) {
		case 0:
			entitysprite = LevelEditor.iconFireEnemy;
			break;
		case 1:
			entitysprite = LevelEditor.iconFireEnemyBlue;
			break;
		case 2:
			entitysprite = LevelEditor.iconThing;
			break;
		case 3:
			entitysprite = LevelEditor.iconManaOrb;
			break;
		case 4:
			entitysprite = LevelEditor.iconPlatform;
			break;
		case 5:
			entitysprite = LevelEditor.iconFlameDemon;
			sizeX = 2;
			sizeY = 2;
			break;
		case 6:
			entitysprite = LevelEditor.iconPuddleMonster;
			break;
		case 7:
			entitysprite = LevelEditor.iconZombieKnight;
			break;
		case 8:
			entitysprite = LevelEditor.iconBomb;
			break;
		case 9:
			entitysprite = LevelEditor.iconBombGuy;
			break;
		default:
			entitysprite = null;
		}
		Main.worldRenderer.drawImage(entitysprite, p.x - 0.5, p.y - 0.5, sizeX, sizeY);
	}
	
	@Override
	public String getIcon() {
		return "/sprites/levelEditor/AddEntity.png";
	}
	
	@Override
	public String getDescription() {
		return "Add Entity";
	}
}
