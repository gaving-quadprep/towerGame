package levelEditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;

public class CheckBoxListener implements ItemListener {
	JCheckBox b1, b2;
	boolean b1selected, b2selected;
	public CheckBoxListener(JCheckBox b1,JCheckBox b2) {
		this.b1=b1;
		this.b2=b2;
		this.b1selected = b1.isSelected();
		this.b2selected = b2.isSelected();
		b1.addItemListener(this);
		b2.addItemListener(this);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == b1) { 
			if (e.getStateChange() == 1) {
				this.b1selected = true;
			}else {
				this.b1selected = false;
			}
		} 
		if (e.getSource() == b2) { 
			if (e.getStateChange() == 1) {
				this.b2selected = true;
			}else {
				this.b2selected = false;
			}
		}
	}
}
