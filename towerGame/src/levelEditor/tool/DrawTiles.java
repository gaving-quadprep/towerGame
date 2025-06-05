package levelEditor.tool;

import levelEditor.LEEventHandler;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import main.Main;
import main.WorldRenderer;
import map.CustomTile;
import map.Level;
import map.Tile;
import map.interactable.TileWithData;
import util.Position;

public class DrawTiles extends Tool {

	@Override
	public void onMouseLeftPressed(LevelEditor le) {
		int[] positions = LevelEditorUtils.getTilePosFromMouse();
		le.level.setTile(positions[0], positions[1], le.eventHandler.tileBrush, !le.eventHandler.editBackground);
		if(LevelEditor.placeTileData != null)
			le.level.setTileData(positions[0], positions[1], LevelEditor.placeTileData, !le.eventHandler.editBackground);
	}

	@Override
	public void onMouseRightPressed(LevelEditor le) {
		int[] positions = LevelEditorUtils.getTilePosFromMouse();
		LEEventHandler eventHandler = le.eventHandler;
		Level level = le.level;
		boolean fromBackground = eventHandler.editBackground;
		eventHandler.tileBrush = level.getTile(positions[0], positions[1], !eventHandler.editBackground);
		if(eventHandler.tileBrush == 0) {
			eventHandler.tileBrush = level.getTileBackground(positions[0], positions[1]);
			fromBackground = true;
		}
		if(Tile.tiles[eventHandler.tileBrush] instanceof TileWithData) {
			LevelEditor.placeTileData = level.getTileData(positions[0], positions[1], !fromBackground);
		}else {
			LevelEditor.placeTileData = null;
		}
	}
	
	@Override
	public void render(LevelEditor le, WorldRenderer wr) {
		LEEventHandler eventHandler = le.eventHandler;
		Position p = LevelEditorUtils.getUnroundedTilePosFromMouse();
		if(eventHandler.tileBrush < 4096) {
			int frameX = (Tile.tiles[eventHandler.tileBrush].getTextureId() % 16) * 16;
			int frameY = (Tile.tiles[eventHandler.tileBrush].getTextureId() / 16) * 16;
			Main.worldRenderer.drawTiledImage(le.level.tilemap, p.x - 0.5, p.y - 0.5, 1, 1, frameX, frameY, frameX+16, frameY+16);
		}else {
			Main.worldRenderer.drawImage(((CustomTile)Tile.tiles[eventHandler.tileBrush]).texture, p.x - 0.5, p.y - 0.5, 1, 1);
		}
	}
	
	@Override
	public String getIcon() {
		return "/sprites/levelEditor/DrawTiles.png";
	}
	
	@Override
	public String getDescription() {
		return "Draw Tiles";
	}

}
