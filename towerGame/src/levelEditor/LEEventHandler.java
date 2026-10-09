package levelEditor;

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
			upPressed=true;
			break;
		case VK_A:
			leftPressed=true;
			break;
		case VK_S:
			downPressed=true;
			break;
		case VK_D:
			rightPressed=true;
			break;
		case VK_SHIFT:
			shiftPressed=true;
			break;
		case VK_UP:
			if(tileBrush < 4096) {
				tileBrush++;
				if(tileBrush>Tile.maxTile) {
					tileBrush=0;
				}
				if(Tile.tiles[tileBrush] instanceof TileWithData)
					LevelEditor.placeTileData = null;
			}
			break;
		case VK_DOWN:
			if(tileBrush < 4096) {
				tileBrush--;
				if(tileBrush<0) {
					tileBrush=Tile.maxTile;
				}
				if(Tile.tiles[tileBrush] instanceof TileWithData)
					LevelEditor.placeTileData = null;
			}
			break;
		case VK_F3:
			debugPressed=!debugPressed;
			break;
		case VK_0:
			tileBrush=0;
			break;
		case VK_1:
			tileBrush=1;
			break;
		case VK_2:
			tileBrush=2;
			break;
		case VK_3:
			tileBrush=3;
			break;
		case VK_4:
			tileBrush=4;
			break;
		case VK_5:
			tileBrush=5;
			break;
		case VK_6:
			tileBrush=6;
			break;
		case VK_7:
			tileBrush=7;
			break;
		case VK_8:
			tileBrush=8;
			break;
		case VK_9:
			tileBrush=9;
			break;
		case VK_F:
			editBackground=!editBackground;
			break;
		case VK_MINUS:
			LevelEditorUtils.zoomOut();
			break;
		case VK_PLUS:
		case VK_EQUALS:
			LevelEditorUtils.zoomIn();
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch(code) {
		case VK_W:
			upPressed=false;
			break;
		case VK_A:
			leftPressed=false;
			break;
		case VK_S:
			downPressed=false;
			break;
		case VK_D:
			rightPressed=false;
			break;
		case VK_SHIFT:
			shiftPressed=false;
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
			mouse1Pressed=true;
			mouse1Clicked=true;
		}
		if(SwingUtilities.isRightMouseButton(arg0)) {
			mouse2Pressed=true;
			mouse2Clicked=true;
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(SwingUtilities.isLeftMouseButton(arg0)) {
			mouse1Pressed=false;
		}
		if(SwingUtilities.isRightMouseButton(arg0)) {
			mouse2Pressed=false;
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e){
		System.out.println("scrolled");
		int rot = e.getWheelRotation();
		if (rot > 0) {
			LevelEditorUtils.zoomOut();
		} else if (rot < 0) {
			LevelEditorUtils.zoomIn();
		}
	}
}