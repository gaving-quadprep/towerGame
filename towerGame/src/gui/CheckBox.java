package gui;

import java.awt.Graphics2D;
import java.awt.Point;

public class CheckBox extends Input<Boolean> {
	public String text;
	private boolean selected;
	@Override
	public Boolean getInput() {
		// TODO Auto-generated method stub
		return selected;
	}
	@Override
	public void render(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onClicked(Point pos) {
		if(pos.x <= 16)
			this.selected = !selected;
	}

}
