package levelEditor;

import java.util.Collection;

import levelEditor.tool.Tool;

@SuppressWarnings("serial")
public class ToolPanel extends EditorPanel {

	public ToolPanel(LevelEditor le) {
		super(le);
		
		Collection<Tool> tools = Tool.toolRegistry.getValues();
		
		for(Tool tool : tools) {
			if(tool.shouldShowInMenu())
				LevelEditorUtils.addButton("Tool;" + Tool.toolRegistry.getName(tool),
						LevelEditorUtils.readImage(tool.getIcon()),
						true,
						tool.getDescription(),
						this);
		}
		
		LevelEditor.addAction("Tool", (args) -> {
			if(args.length < 2)
				return;
			le.tool = Tool.toolRegistry.get(args[1]);
		});
	}

}
	