package levelEditor;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomSpriteEditor extends JPanel {
	private static final long serialVersionUID = 4669936149421499262L;
	LevelEditor le;
	public CustomSpriteEditor(LevelEditor levelEditor) {
		this.le = levelEditor;
		this.add(new JTextField());
	}
}
