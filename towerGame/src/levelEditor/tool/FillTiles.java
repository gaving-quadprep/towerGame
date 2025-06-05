package levelEditor.tool;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import main.Main;
import main.WorldRenderer;
import util.Position;

public class FillTiles extends DrawTiles {
	public boolean slowFloodFill = false;
	public boolean stackFloodFill = true;
	
	@Override
	public void onMouseLeftPressed(LevelEditor le) {
		int[] positions = LevelEditorUtils.getTilePosFromMouse();
		le.level.floodFill(positions[0], positions[1], le.eventHandler.tileBrush, !le.eventHandler.editBackground);
	}

	@Override
	public void render(LevelEditor le, WorldRenderer wr) {
		super.render(le, wr);
		Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
		Main.worldRenderer.drawImage(LevelEditor.fillTool, p.x - 1, p.y - 1, 1, 1);
	}
	
	@Override
	public String getIcon() {
		return "/sprites/levelEditor/FillTiles.png";
	}
	
	@Override
	public String getDescription() {
		return "Fill Tiles";
	}
}
