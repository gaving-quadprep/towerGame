package levelEditor.menu;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.panel.PlayerPanel;
import util.Position;
import weapon.Weapon;

@SuppressWarnings("serial")
public class PlayerMenu extends EditorMenu {

	public PlayerMenu(LevelEditor le, String name) {
		super(le, name);
		
		LevelEditorUtils.addMenuItem(this, "Change Start", KeyEvent.VK_S);
		LevelEditorUtils.addMenuItem(this, "Change Health", KeyEvent.VK_H);
		LevelEditorUtils.addMenuItem(this, "Change Mana", KeyEvent.VK_M);
		LevelEditorUtils.addMenuItem(this, "Change Weapon", KeyEvent.VK_W);
		LevelEditorUtils.addMenuItem(this, "Change Speed", KeyEvent.VK_P);

		LevelEditor.addAction("Change Start", (args) -> {
			Position pos = LevelEditorUtils.promptCoordinates("Player start position");
			if(pos != null) {
				le.level.playerStartX = pos.x;
				le.level.playerStartY = pos.y;
				LevelEditor.playerPanel.updateValues(PlayerPanel.START_POS);
			}
		});
		LevelEditor.addAction("Change Health", (args) -> {
			String userInput = JOptionPane.showInputDialog(null, "Player health:", "Change Player Health", JOptionPane.QUESTION_MESSAGE);
			if(userInput != null) {
				LevelEditor.playerHealth = Double.parseDouble(userInput);
				LevelEditor.playerPanel.updateValues(PlayerPanel.HEALTH);
			}
		});
		LevelEditor.addAction("Change Mana", (args) -> {
			String userInput = JOptionPane.showInputDialog(null, "Player mana:", "Change Player Mana", JOptionPane.QUESTION_MESSAGE);
			if(userInput != null) {
				LevelEditor.playerMana = Double.parseDouble(userInput);
				LevelEditor.playerPanel.updateValues(PlayerPanel.MANA);
			}
		});
		LevelEditor.addAction("Change Weapon", (args) -> {
			String[] possibleValues = new String[] {"Staff", "Level 2 Staff", "Level 3 Staff", "Shield", "Sword", "Dagger", "Pickaxe", "No Weapon"};
			
			String result = (String) JOptionPane.showInputDialog(null,
						 "Choose an weapon", "Change Player Weapon",
						 JOptionPane.INFORMATION_MESSAGE, null,
						 possibleValues, possibleValues[0]);
			if(result == null) 
				return;
			switch(result) {
			case "No Weapon":
				LevelEditor.playerWeapon = 0; 
				break;
			case "Staff":
				LevelEditor.playerWeapon = Weapon.staff.id; 
				break;
			case "Level 2 Staff":
				LevelEditor.playerWeapon = Weapon.staffUpgraded.id; 
				break;
			case "Level 3 Staff":
				LevelEditor.playerWeapon = Weapon.staffUpgraded2.id; 
				break;
			case "Shield":
				LevelEditor.playerWeapon = Weapon.shield.id; 
				break;
			case "Sword":
				LevelEditor.playerWeapon = Weapon.sword.id; 
				break;
			case "Dagger":
				LevelEditor.playerWeapon = Weapon.dagger.id; 
				break;
			case "Pickaxe":
				LevelEditor.playerWeapon = Weapon.pickaxe.id; 
				break;
			}
			LevelEditor.playerPanel.updateValues(PlayerPanel.WEAPON);
		});
		LevelEditor.addAction("Change Speed", (args) -> {
			String userInput = JOptionPane.showInputDialog(null, "Player speed:", "Change Player Speed", JOptionPane.QUESTION_MESSAGE);
			if(userInput != null) {
				LevelEditor.playerSpeed = Double.parseDouble(userInput);
				LevelEditor.playerPanel.updateValues(PlayerPanel.SPEED);
			}
		});
	}

}
