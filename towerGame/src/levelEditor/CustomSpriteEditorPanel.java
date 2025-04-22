package levelEditor;

import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomSpriteEditorPanel extends EditorPanel {
	private static final long serialVersionUID = 4669936149421499262L;
	LevelEditor le;
	public CustomSpriteEditorPanel(LevelEditor le) {
		super(le);
	}
	public void initialize() {
		this.add(new JTextField());
		this.setLayout(new GridBagLayout());
	}
}
