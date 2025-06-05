package levelEditor.tool;

import java.awt.Rectangle;

import entity.Entity;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import util.CollisionChecker;
import util.Position;

public class RemoveEntity extends Tool {
	private static Rectangle mp = new Rectangle(0, 0, 2, 2);
	@Override
	public void onMouseLeftClick(LevelEditor le) {
		
		Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
		Entity[] entities = le.level.getEntityArray();
		for (int i = entities.length - 1; i >= 0; i--) { // Remove the one on top
			Entity e = entities[i];
			if(CollisionChecker.checkHitboxes(mp, e.hitbox, p.x, p.y, e.x, e.y)) {
				e.markedForRemoval = true;
				if(!le.eventHandler.shiftPressed) // holding shift deletes all entities
					break;
			}
		}
	}
	
	@Override
	public String getIcon() {
		return "/sprites/levelEditor/RemoveEntity.png";
	}
	
	@Override
	public String getDescription() {
		return "RemoveEntity";
	}
}
