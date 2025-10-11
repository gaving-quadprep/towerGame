package levelEditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

public class CheckBoxListener implements ItemListener {
	JCheckBox[] checkboxes;
	public boolean[] selected;
	public CheckBoxListener(JCheckBox[] cb) {
		this.checkboxes = cb;
		this.selected = new boolean[cb.length];
		for(int i = 0;i < checkboxes.length; i++) {	
			checkboxes[i].addItemListener(this);
		}
		for(int i = 0;i < checkboxes.length; i++) {	
			if (this.checkboxes[i].isSelected()) {
				this.selected[i] = true;
			}else {
				this.selected[i] = false;
			}
		}
	}

	public void itemStateChanged(ItemEvent e) {
		for(int i = 0; i < checkboxes.length; i++) {	
			if (e.getSource() == checkboxes[i]) {
				if (e.getStateChange() == 1) {
					this.selected[i] = true;
				}else {
					this.selected[i] = false;
				}
				
			}
		}
	}
}
