package gui;

import java.awt.Font;
import java.awt.Graphics2D;

public class FontRenderer {
	Font font;
	public FontRenderer(String fontName) {
	    try{
	      this.font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/"+fontName+".ttf")).deriveFont(6.0f);
	    } catch (Exception e){
	    	e.printStackTrace();
	    }
	}
	public void drawText(Graphics2D g2, String text, int x, int y) {
		g2.setFont(this.font);
		g2.drawString(text, x, y);
	}
}
