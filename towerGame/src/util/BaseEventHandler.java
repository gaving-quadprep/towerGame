package util;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public abstract class BaseEventHandler extends MouseAdapter implements KeyListener {
	public int mousePosX;
	public int mousePosY;
	@Override
	public void mouseDragged(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}

	@Override
	public void mouseExited(MouseEvent e) {
		mousePosX = e.getX();
		mousePosY = e.getY();

	}
	
	public Point getMousePos() {
		return new Point(mousePosX, mousePosY);
	}
}
