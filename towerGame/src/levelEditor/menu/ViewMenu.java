package levelEditor.menu;

import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JMenu;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.panel.EditorPanel;

@SuppressWarnings("serial")
public class ViewMenu extends EditorMenu {
	public ViewMenu(final LevelEditor le, String name) {
		super(le, name);

		LevelEditorUtils.addCheckBoxMenuItem(this, "Tile", "ToggleView;TilePanel", true);
		LevelEditorUtils.addCheckBoxMenuItem(this, "Entity", "ToggleView;EntityPanel", true);
		LevelEditorUtils.addCheckBoxMenuItem(this, "Tool", "ToggleView;ToolPanel", true);
		LevelEditorUtils.addCheckBoxMenuItem(this, "Player", "ToggleView;PlayerPanel", false);
		
		JMenu showAsSubMenu = new JMenu("Show As");
		this.addSeparator();
		this.add(showAsSubMenu);
		ButtonGroup group = new ButtonGroup();
		LevelEditorUtils.addRadioButtonMenuItem(showAsSubMenu, "Name", "ShowAs;Name", group, false);
		LevelEditorUtils.addRadioButtonMenuItem(showAsSubMenu, "Icon", "ShowAs;Icon", group, false);
		LevelEditorUtils.addRadioButtonMenuItem(showAsSubMenu, "Name And Icon", "ShowAs;NameAndIcon", group, true);
		
		LevelEditor.addAction("ToggleView", new LevelEditor.Action() {
			@Override
			public void run(String[] args) {
				if(args[1].equals("TilePanel"))
					LevelEditor.tilePanel.toggle();
				if(args[1].equals("EntityPanel"))
					LevelEditor.entityPanel.toggle();
				if(args[1].equals("ToolPanel"))
					LevelEditor.toolPanel.toggle();
				if(args[1].equals("PlayerPanel"))
					LevelEditor.playerPanel.toggle();
			}
		});
		
		LevelEditor.addAction("ShowAs", new LevelEditor.Action() {
			@Override
			public void run(String[] args) {
				if(args.length < 2)
					return;
				if(args[1].equals("Name")) {
					le.showName = true;
					le.showIcon = false;
				}
				if(args[1].equals("Icon")) {
					le.showName = false;
					le.showIcon = true;
				}
				if(args[1].equals("NameAndIcon")) {
					le.showName = true;
					le.showIcon = true;
				}
				for (int i = 0; i < LevelEditor.tabbedPane.getTabCount(); i++) {
					EditorPanel ep = (EditorPanel) LevelEditor.tabbedPane.getComponentAt(i);
					LevelEditor.tabbedPane.setTitleAt(i, le.showName ? ep.getName() : "");
					
	
					ImageIcon icon = null;
					if(le.showIcon) {
						BufferedImage image = LevelEditorUtils.readImage(ep.getIcon());
						icon = image == null ? null : new ImageIcon(image);
					}
					LevelEditor.tabbedPane.setIconAt(i, icon);
				}
			}
		});
	}

}
