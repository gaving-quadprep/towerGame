package gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import main.Main;

public class FontRenderer {
	BufferedImage font;
	Glyph[] glyphs = new Glyph[256];
	private static class Glyph {
		BufferedImage image;
		int width;
	}
	public FontRenderer(String filePath) {
		try{
		  this.font = ImageIO.read(getClass().getResourceAsStream(filePath));
		} catch (Exception e){
			e.printStackTrace();
		}
		for(char i=' ';i<128;i++) {
			BufferedImage image = new BufferedImage(5, 6, BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(font, (i-' ')*-5, 0, null);
			Rectangle rect = LevelEditorUtils.autoGetHitbox(image);
			glyphs[i] = new Glyph();
			glyphs[i].image = image;
			glyphs[i].width = rect.width;
		}
	}
	public FontRenderer() {
		this("/font.png");
	}
	public void drawText(Graphics2D g2, String text, int x, int y) {
		//Composite oldComposite = g2.getComposite();
		//g2.setXORMode(Color.BLACK);
		int x1 = x;
		for(char c : text.toCharArray()) {
			if (c == '\n') {
				x1 = x;
				y += 7*Main.scale;
			} else {
				drawChar(g2, c, x1, y);
				x1 += glyphs[c].width * Main.scale;
				x1 += Main.scale;
			}
		}
		//g2.setComposite(oldComposite);
	}
	public void drawTextCentered(Graphics2D g2, String text, int x, int y) {
		drawText(g2, text, x - (getWidth(text)/2) * Main.scale, y);
	}
	public void drawChar(Graphics2D g2, char c, int x, int y) {
		g2.drawImage(glyphs[c].image, x, y, 5*Main.scale, 6*Main.scale, null);
	}
	public int getWidth(char c) {
		return glyphs[c].width;
	}
	public int getWidth(String s) {
		int width = 0;
		for(char c : s.toCharArray()) {
			width += getWidth(c);
			width++;
		}
		width--;
		return width;
	}
}
