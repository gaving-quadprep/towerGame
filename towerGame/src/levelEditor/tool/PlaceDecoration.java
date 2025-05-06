package levelEditor.tool;

import entity.Decoration;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import main.Main;
import main.WorldRenderer;
import util.Position;

public class PlaceDecoration extends AddEntity {
	@Override
	public boolean shouldShowInMenu() {
		return false;
	}
	
	@Override
	public void onMouseLeftClick(LevelEditor le) {
		Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
		if(le.placeableDecoration != null) {
			Decoration decoration;
			decoration = (Decoration) le.placeableDecoration.clone();
			decoration.setPosition(p.x - 0.5, p.y - 0.5);
			le.level.addEntity(decoration);
		}
	}
	
	@Override
	public void render(LevelEditor le, WorldRenderer wr) {
		if(le.placeableDecoration != null) {
			Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
			Main.worldRenderer.drawImage(le.placeableDecoration.sprite, p.x - 0.5, p.y - 0.5, le.placeableDecoration.imageSizeX / 16.0, le.placeableDecoration.imageSizeY / 16.0);
		}
	}
	
	@Override
	public String getIcon() {
		return "/sprites/levelEditor/AddDecoration.png";
	}
	
	@Override
	public String getDescription() {
		return "Place Decoration";
	}
}
