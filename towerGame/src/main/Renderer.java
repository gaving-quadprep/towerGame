package main;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

public class Renderer {
	
	public static GraphicsConfiguration currentGraphicsConfiguration;
	
	public static BufferedImage createImage(int w, int h) {
		if (currentGraphicsConfiguration != null)
			return currentGraphicsConfiguration.createCompatibleImage(w, h);
		return new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
	}
	
	public void setLayer(int layer) {
		
	}
	
}