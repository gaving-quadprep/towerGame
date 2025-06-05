package levelEditor.tool;

import levelEditor.LevelEditor;
import main.WorldRenderer;
import util.Registry;

public abstract class Tool {
	public void onMouseLeftClick(LevelEditor le) {}
	public void onMouseRightClick(LevelEditor le) {}
	public void onMouseLeftPressed(LevelEditor le) {}
	public void onMouseRightPressed(LevelEditor le) {}
	public void update(LevelEditor le) {}
	public void render(LevelEditor le, WorldRenderer wr) {}

	public String getIcon() {
		return null;
	}
	
	public String getDescription() {
		return "A tool";
	}
	
	public boolean shouldShowInMenu() {
		return true;
	}
	
	public static final Registry<Tool> toolRegistry = new Registry<Tool>(true);
	
	public static final Tool drawTiles = new DrawTiles();
	public static final Tool fillTiles = new FillTiles();
	public static final Tool addEntity = new AddEntity();
	public static final Tool moveEntity = new MoveEntity();
	public static final Tool removeEntity = new RemoveEntity();
	public static final Tool placeDecoration = new PlaceDecoration();
	
	static {
		toolRegistry.addMapping(drawTiles, "drawTiles");
		toolRegistry.addMapping(fillTiles, "fillTiles");
		toolRegistry.addMapping(addEntity, "addEntity");
		toolRegistry.addMapping(moveEntity, "moveEntity");
		toolRegistry.addMapping(removeEntity, "removeEntity");
		toolRegistry.addMapping(placeDecoration, "placeDecoration");
	}
}
