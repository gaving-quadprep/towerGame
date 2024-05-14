package util;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class LineBreak extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3193435865085457211L;

	@Override
	public int getWidth() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(Integer.MAX_VALUE, 0);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(Integer.MAX_VALUE, 0);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(Integer.MAX_VALUE, 0);
	}
}
