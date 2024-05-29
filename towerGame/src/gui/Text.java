package gui;

import java.awt.Graphics2D;

public class Text extends UIComponent {
	public String text;
	@Override
	public void render(Graphics2D g2) {
		GUI.fontRenderer.drawText(g2, this.text, this.x, this.y);
		// TODO Auto-generated method stub
		
	}
}
