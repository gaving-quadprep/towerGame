package towerGame;

import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.DebugScreen;
import gui.InventoryGUI;
import gui.SpellMenuGUI;
import main.Main;
import util.BaseEventHandler;

public class EventHandler extends BaseEventHandler {
	public boolean upPressed = false;
	public boolean downPressed = false;
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public boolean shiftPressed = false;
	public boolean showDebug = false;
	public boolean showEntityDebug = false;
	public boolean mouse1Pressed = false;
	public boolean mouse2Pressed = false;
	public boolean mouse1Clicked = false;
	public boolean mouse2Clicked = false;
	public boolean paused = false;
	public boolean resetPressed = false;
	public JFrame frame;
	public EventHandler(JFrame frame) {
		super();
		this.frame=frame;
		//this.requestFocus();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code=e.getKeyCode();
		switch(code) {
		case VK_W:
		case VK_UP:
		case VK_SPACE:
			this.upPressed=true;
			break;
		case VK_A:
		case VK_LEFT:
			this.leftPressed=true;
			break;
		case VK_S:
		case VK_DOWN:
			this.downPressed=true;
			break;
		case VK_D:
		case VK_RIGHT:
			this.rightPressed=true;
			break;
		case KeyEvent.VK_F3:
			if(shiftPressed)
				this.showEntityDebug = !showEntityDebug;
			else {
				this.showDebug = !showDebug;
				TowerGame.toggle(new DebugScreen());
			}
			break;

		case KeyEvent.VK_F9:
			if (TowerGame.isTesting) {
				if(shiftPressed) {
					Main.fpsCap *= 2;
				} else {
					Main.fpsCap /= 2;
				}
			}
			break;
		case VK_SHIFT:
			this.shiftPressed=true;
			break;
		case VK_ESCAPE:
			this.paused=!paused;
			if(paused) {
				TowerGame.show(TowerGame.pauseMenu);
			}else {
				TowerGame.hide(TowerGame.pauseMenu);
			}
			break;
		case VK_R:
			resetPressed = true;
			break;
		case VK_V:
			TowerGame.toggle(new InventoryGUI());
			break;
		case VK_Z:
			TowerGame.toggle(new SpellMenuGUI());
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code=e.getKeyCode();
		switch(code) {
		case VK_W:
		case VK_UP:
		case VK_SPACE:
			this.upPressed = false;
			break;
		case VK_A:
		case VK_LEFT:
			this.leftPressed = false;
			break;
		case VK_S:
		case VK_DOWN:
			this.downPressed = false;
			break;
		case VK_D:
		case VK_RIGHT:
			this.rightPressed = false;
			break;
		case VK_R:
			resetPressed = false;
			break;
		case VK_SHIFT:
			this.shiftPressed=false;
			break;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0)) {
			this.mouse1Pressed=true;
			this.mouse1Clicked=true;
		}
		if(SwingUtilities.isRightMouseButton(arg0)) {
			this.mouse2Pressed=true;
			this.mouse2Clicked=true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0)) {
			this.mouse1Pressed=false;
		}
		if(SwingUtilities.isRightMouseButton(arg0)) {
			this.mouse2Pressed=false;
		}
	}
}