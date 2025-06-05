package levelEditor.menu;

import javax.swing.JMenu;

import levelEditor.LevelEditor;

@SuppressWarnings("serial")
public abstract class EditorMenu extends JMenu {
	LevelEditor le;
	public EditorMenu(LevelEditor le, String name) {
		super(name);
		this.le = le;
	}
}
