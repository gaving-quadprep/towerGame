package levelEditor;

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import map.Tile;
import map.interactable.TileWithData;
import util.BaseEventHandler;

public class LEEventHandler extends BaseEventHandler {
	public boolean upPressed = false;
	public boolean downPressed = false;
	public boolean leftPressed = false;
	public boolean rightPressed = false;
	public boolean shiftPressed = false;
	public boolean debugPressed = false;
	public boolean mouse1Pressed = false;
	public boolean mouse2Pressed = false;
	public boolean mouse1Clicked = false;
	public boolean mouse2Clicked = false;
	public boolean editBackground = false;
	public int tileBrush = 1;
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
		int code = e.getKeyCode();
		switch(code) {
		case VK_W:
			this.upPressed=true;
			break;
		case VK_A:
			this.leftPressed=true;
			break;
		case VK_S:
			this.downPressed=true;
			break;
		case VK_D:
			this.rightPressed=true;
			break;
		case VK_SHIFT:
			this.shiftPressed=true;
			break;
		case VK_UP:
			if(tileBrush < 4096) {
				this.tileBrush++;
				if(tileBrush>Tile.maxTile) {
					tileBrush=0;
				}
				if(Tile.tiles[this.tileBrush] instanceof TileWithData)
					LevelEditor.placeTileData = null;
			}
			break;
		case VK_DOWN:
			if(tileBrush < 4096) {
				this.tileBrush--;
				if(tileBrush<0) {
					tileBrush=Tile.maxTile;
				}
				if(Tile.tiles[this.tileBrush] instanceof TileWithData)
					LevelEditor.placeTileData = null;
			}
			break;
		case VK_F3:
			this.debugPressed=!debugPressed;
			break;
		case VK_0:
			this.tileBrush=0;
			break;
		case VK_1:
			this.tileBrush=1;
			break;
		case VK_2:
			this.tileBrush=2;
			break;
		case VK_3:
			this.tileBrush=3;
			break;
		case VK_4:
			this.tileBrush=4;
			break;
		case VK_5:
			this.tileBrush=5;
			break;
		case VK_6:
			this.tileBrush=6;
			break;
		case VK_7:
			this.tileBrush=7;
			break;
		case VK_8:
			this.tileBrush=8;
			break;
		case VK_9:
			this.tileBrush=9;
			break;
		case VK_F:
			this.editBackground=!this.editBackground;
			break;
		case VK_MINUS:
			LevelEditor.zoomOut();
			break;
		case VK_PLUS:
		case VK_EQUALS:
			LevelEditor.zoomIn();
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch(code) {
		case VK_W:
			this.upPressed=false;
			break;
		case VK_A:
			this.leftPressed=false;
			break;
		case VK_S:
			this.downPressed=false;
			break;
		case VK_D:
			this.rightPressed=false;
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