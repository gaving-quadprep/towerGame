package levelEditor.tool;

import java.awt.Rectangle;

import entity.Entity;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import util.CollisionChecker;
import util.Position;

public class MoveEntity extends Tool {
	private static Rectangle mp = new Rectangle(0, 0, 2, 2);
	Entity movingEntity = null;
	double offsetX = 0, offsetY = 0;
	
	@Override
	public void onMouseLeftClick(LevelEditor le) {
		Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
		
		if(movingEntity == null) {
			Entity[] entities = le.level.getEntityArray();
			for (int i = entities.length - 1; i >= 0; i--) {
				Entity e = entities[i];
				if(CollisionChecker.checkHitboxes(mp, e.hitbox, p.x, p.y, e.x, e.y)) {
					movingEntity = e;
					offsetX = p.x - e.x;
					offsetY = p.y - e.y;
					break;
				}
			}
		} else {
			movingEntity = null;
		}
	}
	
	@Override
	public void update(LevelEditor le) {
		if(movingEntity != null) {
			Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
			movingEntity.setPosition(p.x - offsetX, p.y - offsetY);
		}
	}
	
	@Override
	public String getIcon() {
		return "/sprites/levelEditor/MoveEntity.png";
	}

	@Override
	public String getDescription() {
		return "Move Entity";
	}
}
