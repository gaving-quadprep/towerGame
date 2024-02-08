package levelEditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LEEventHandler implements MouseListener, KeyListener{
	public boolean upPressed=false;
	public boolean downPressed=false;
	public boolean leftPressed=false;
	public boolean rightPressed=false;
	public boolean debugPressed=false;
	public boolean mouseCoordsTool=false;
	public boolean mouse1Pressed=false;
	public boolean mouse2Pressed=false;
	public boolean mouse1Clicked=false;
	public boolean mouse2Clicked=false;
	public boolean editBackground=false;
	public int tileBrush=1;
	public JFrame frame;
	public LEEventHandler(JFrame frame) {
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
		if(code==KeyEvent.VK_UP) {
			this.tileBrush++;
			if(tileBrush>46) {
				tileBrush=0;
			}
		}
		if(code==KeyEvent.VK_DOWN) {
			this.tileBrush--;
			if(tileBrush<0) {
				tileBrush=46;
			}
		}
		if(code==KeyEvent.VK_F3) {
			this.debugPressed=!debugPressed;
		}
		if(code==KeyEvent.VK_0) {
			this.tileBrush=0;
		}
		if(code==KeyEvent.VK_1) {
			this.tileBrush=1;
		}
		if(code==KeyEvent.VK_2) {
			this.tileBrush=2;
		}
		if(code==KeyEvent.VK_3) {
			this.tileBrush=3;
		}
		if(code==KeyEvent.VK_4) {
			this.tileBrush=4;
		}
		if(code==KeyEvent.VK_5) {
			this.tileBrush=5;
		}
		if(code==KeyEvent.VK_6) {
			this.tileBrush=6;
		}
		if(code==KeyEvent.VK_7) {
			this.tileBrush=7;
		}
		if(code==KeyEvent.VK_8) {
			this.tileBrush=8;
		}
		if(code==KeyEvent.VK_9) {
			this.tileBrush=9;
		}
		if(code==KeyEvent.VK_F) {
			this.editBackground=!this.editBackground;
		}
		if(code==KeyEvent.VK_C) {
			this.mouseCoordsTool=!this.mouseCoordsTool;
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
		if(code==KeyEvent.VK_UP) {
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
	public void getMousePos() {
		
	}
}