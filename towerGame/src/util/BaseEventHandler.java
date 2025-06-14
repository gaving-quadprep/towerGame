package util;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public abstract class BaseEventHandler implements KeyListener, MouseListener, MouseMotionListener {
	public int mousePosX;
	public int mousePosY;
	@Override
	public void mouseDragged(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		this.mousePosX = e.getX();
		this.mousePosY = e.getY();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public Point getMousePos() {
		return new Point(this.mousePosX, this.mousePosY);
	}
}
