package gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import levelEditor.LevelEditor;
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
		for(int i=' ';i<128;i++) {
			BufferedImage image = new BufferedImage(5, 6, BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(font, (i-' ')*-5, 0, null);
			Rectangle rect = LevelEditor.autoGetHitbox(image);
			glyphs[i] = new Glyph();
			glyphs[i].image = image;
			glyphs[i].width = rect.width;
		}
	}
	public FontRenderer() {
		this("/font.png");
	}
	public void drawText(Graphics2D g2, String text, int x, int y) {
		for(char c : text.toCharArray()) {
			drawChar(g2, c, x, y);
			x += glyphs[c].width * Main.scale;
			x += Main.scale;
		}
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
