package levelEditor.menu;

import java.awt.event.KeyEvent;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;

@SuppressWarnings("serial")
public class TileMenu extends EditorMenu {

	public TileMenu(LevelEditor le, String name) {
		super(le, name);
		
		LevelEditorUtils.addMenuItem(this, "Clear Custom Tiles", KeyEvent.VK_C);
		
		LevelEditor.addAction("Clear Custom Tiles", (actionArgs) -> LevelEditorUtils.clearCustomTiles());
		
		// TODO Auto-generated constructor stub
	}

}
