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
	
		for (int i = le.level.entities.size() - 1; i >= 0; i--) { // Remove the one on top
			Entity e2 = le.level.entities.get(i);
			if(CollisionChecker.checkHitboxes(mp, e2.hitbox, p.x, p.y, e2.x, e2.y)) {
				e2.markedForRemoval = true;
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
