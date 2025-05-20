package levelEditor.panel;

import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.tool.Tool;

@SuppressWarnings("serial")
public class ToolPanel extends EditorPanel {

	public ToolPanel(LevelEditor le) {
		super(le);
		
		this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel innerToolPanel = new JPanel();
		innerToolPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		this.add(innerToolPanel);
		
		Collection<Tool> tools = Tool.toolRegistry.getValues();
		
		for(Tool tool : tools) {
			if(tool.shouldShowInMenu())
				LevelEditorUtils.addButton("Tool;" + Tool.toolRegistry.getName(tool),
						LevelEditorUtils.readImage(tool.getIcon()),
						true,
						tool.getDescription(),
						innerToolPanel);
		}
		
		LevelEditor.addAction("Tool", (args) -> {
			if(args.length < 2)
				return;
			le.tool = Tool.toolRegistry.get(args[1]);
		});
	}
	
	public String getName() {
		return "Tool";
	}
	
	public String getIcon() {
		return "/sprites/levelEditor/Tool.png";
	}

}
	