package levelEditor;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class EditorPanel extends JPanel {
	LevelEditor le;
	public EditorPanel(LevelEditor le) {
		this.le = le;
	}
}
