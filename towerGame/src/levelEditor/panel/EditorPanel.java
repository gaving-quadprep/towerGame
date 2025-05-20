package levelEditor.panel;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;

@SuppressWarnings("serial")
public abstract class EditorPanel extends JPanel {
	LevelEditor le;
	public EditorPanel(LevelEditor le) {
		this.le = le;
	}

	public String getIcon() {
		return null;
	}
	
	public String getName() {
		return "";
	}
	
	public void toggle() {
		if (this.getParent() != LevelEditor.tabbedPane) {
			String name = le.showName ? getName() : "";
			ImageIcon icon = null;
			if(le.showIcon) {
				BufferedImage image = LevelEditorUtils.readImage(getIcon());
				icon = image == null ? null : new ImageIcon(image);
			}
			if (icon != null) {
				LevelEditor.tabbedPane.addTab(name, icon, this);
			} else {
				LevelEditor.tabbedPane.addTab(name, this);
			}
		} else {
			LevelEditor.tabbedPane.remove(this);
		}
	}
}
