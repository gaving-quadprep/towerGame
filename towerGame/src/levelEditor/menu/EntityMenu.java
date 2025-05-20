package levelEditor.menu;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import entity.Entity;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import map.Level;
import util.Position;

@SuppressWarnings("serial")
public class EntityMenu extends EditorMenu {

	public EntityMenu(LevelEditor le, String name) {
		super(le, name);
		
		LevelEditorUtils.addMenuItem(this, "Add Entity", KeyEvent.VK_A);
		LevelEditorUtils.addMenuItem(this, "Remove Entity", KeyEvent.VK_R);
		LevelEditorUtils.addMenuItem(this, "Clear All Entities", KeyEvent.VK_X);
		
		LevelEditor.addAction("Add Entity", (args) -> {
			String userInput = JOptionPane.showInputDialog(null, "Entity type", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			if(userInput != null) {
				Entity entity;
				if(userInput.equals("Decoration") && le.placeableDecoration != null) {
					entity = le.placeableDecoration;
				} else {
					entity = Entity.entityRegistry.createByName(userInput, new Class[] {Level.class}, new Object[] {le.level});
				}
				if(entity != null) {
					Position p = LevelEditorUtils.promptCoordinates("Entity position");
					entity.setPosition(p);
					le.level.addEntity(entity);
					
				} else {
					JOptionPane.showMessageDialog(null, "Invalid entity type", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		LevelEditor.addAction("Remove Entity", (args) -> {
			if(le.level.entities.size() > 0) {
				Object[] possibleValues = le.level.entities.toArray();

				Object en = JOptionPane.showInputDialog(null,
							 "Choose an entity", "Remove Entity",
							 JOptionPane.INFORMATION_MESSAGE, null,
							 possibleValues, possibleValues[0]);
				le.level.entities.remove(en);
			} else {
				JOptionPane.showMessageDialog(null, "No entities to remove", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		LevelEditor.addAction("Clear All Entities", (args) -> {
			int response = JOptionPane.showConfirmDialog(le, "Are you sure you want to clear all entities?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION)
				le.level.entities.clear();
		});
	}

}
