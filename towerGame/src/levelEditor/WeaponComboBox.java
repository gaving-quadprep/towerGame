package levelEditor;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class WeaponComboBox extends JComboBox<String> {
	public static String[] inputs = new String[] {"No Weapon", "Staff", "Level 2 Staff", "Level 3 Staff", "Shield", "Sword", "Dagger", "Pickaxe"};
	
	public WeaponComboBox() {
		super(inputs);
		this.setSelectedIndex(1);
	}
}
