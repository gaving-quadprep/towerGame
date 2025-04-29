package levelEditor;

import javax.swing.JPanel;

public abstract class EditorPanel extends JPanel {
	LevelEditor le;
	public EditorPanel(LevelEditor le) {
		this.le = le;
	}
}
