package towerGame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gui.DebugScreen;
import gui.InventoryGUI;
import gui.SpellMenuGUI;
import gui.TileInteractionGUI;
public class EventHandler implements MouseListener,KeyListener{
	public boolean upPressed=false;
	public boolean downPressed=false;
	public boolean leftPressed=false;
	public boolean rightPressed=false;
	public boolean debugPressed=false;
	public boolean mouse1Pressed=false;
	public boolean mouse2Pressed=false;
	public boolean mouse1Clicked=false;
	public boolean mouse2Clicked=false;
	public boolean paused=false;
	public boolean resetPressed=false;
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
		if(code==KeyEvent.VK_W) {
			this.upPressed=true;
		}
		if(code==KeyEvent.VK_A) {
			this.leftPressed=true;
		}
		if(code==KeyEvent.VK_S) {
			this.downPressed=true;
		}
		if(code==KeyEvent.VK_D) {
			this.rightPressed=true;
		}
		if(code==KeyEvent.VK_R) {
			this.resetPressed=true;
		}
		if(code==KeyEvent.VK_UP) {
			this.upPressed=true;
		}
		if(code==KeyEvent.VK_SPACE) {
			this.upPressed=true;
		}
		if(code==KeyEvent.VK_LEFT) {
			this.leftPressed=true;
		}
		if(code==KeyEvent.VK_DOWN) {
			this.downPressed=true;
		}
		if(code==KeyEvent.VK_RIGHT) {
			this.rightPressed=true;
		}
		if(code==KeyEvent.VK_F3) {
			this.debugPressed=!debugPressed;
			TowerGame.toggle(new DebugScreen());
		}
		if(code==KeyEvent.VK_ESCAPE) {
			this.paused=!paused;
			if(paused) {
				TowerGame.show(TowerGame.pauseMenu);
			}else {
				TowerGame.hide(TowerGame.pauseMenu);
			}
		}
		if(code==KeyEvent.VK_V) {
			TowerGame.toggle(new InventoryGUI());
		}
		if(code==KeyEvent.VK_Z) {
			TowerGame.toggle(new SpellMenuGUI());
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code=e.getKeyCode();
		if(code==KeyEvent.VK_W) {
			this.upPressed=false;
		}
		if(code==KeyEvent.VK_A) {
			this.leftPressed=false;
		}
		if(code==KeyEvent.VK_S) {
			this.downPressed=false;
		}
		if(code==KeyEvent.VK_D) {
			this.rightPressed=false;
		}
		if(code==KeyEvent.VK_R) {
			this.resetPressed=false;
		}
		if(code==KeyEvent.VK_UP) {
			this.upPressed=false;
		}
		if(code==KeyEvent.VK_SPACE) {
			this.upPressed=false;
		}
		if(code==KeyEvent.VK_LEFT) {
			this.leftPressed=false;
		}
		if(code==KeyEvent.VK_DOWN) {
			this.downPressed=false;
		}
		if(code==KeyEvent.VK_RIGHT) {
			this.rightPressed=false;
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
	public Point getMousePos() {
		Point point = MouseInfo.getPointerInfo().getLocation();
		return new Point((int) (point.x - TowerGame.gamePanel.frame.getLocation().x), (int) (point.y - TowerGame.gamePanel.frame.getLocation().y));
	}
}