package levelEditor.menu;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import main.Main;
import map.Level;
import map.interactable.TileData;
import save.SaveFile;
import towerGame.TowerGame;
import util.Position;

@SuppressWarnings("serial")
public class WorldMenu extends EditorMenu {

	public WorldMenu(LevelEditor le, String name) {
		super(le, name);
		
		LevelEditorUtils.addMenuItem(this, "Resize Level", KeyEvent.VK_R);
		LevelEditorUtils.addMenuItem(this, "Change Sky Color", KeyEvent.VK_C);
		LevelEditorUtils.addMenuItem(this, "Test", KeyEvent.VK_T);
		LevelEditorUtils.addMenuItem(this, "Zoom In", KeyEvent.VK_I);
		LevelEditorUtils.addMenuItem(this, "Zoom Out", KeyEvent.VK_O);
		LevelEditorUtils.addMenuItem(this, "Change Gravity", KeyEvent.VK_G);
		
		LevelEditor.addAction("Resize Level", (args) -> {
			Level level = le.level;
			int levelSizeX, levelSizeY;
			
			if(args.length >= 3) {
				levelSizeX = Integer.parseInt(args[1]);
				levelSizeY = Integer.parseInt(args[2]);
			} else {
				Position size = LevelEditorUtils.promptCoordinates("New size");
				if (size != null) {
					levelSizeX = (int) size.x;
					levelSizeY = (int) size.y;
				} else {
					return;
				}
			}
			int[][] oldBackground = level.mapTilesBackground;
			int[][] oldForeground = level.mapTilesForeground;
			TileData[][] oldTDBackground = level.tileDataBackground;
			TileData[][] oldTDForeground = level.tileDataForeground;
			
			int oldSizeX = level.sizeX;
			int oldSizeY = level.sizeY;
			
			level.sizeX = levelSizeX;
			level.sizeY = levelSizeY;
			
			level.mapTilesBackground = new int[levelSizeX][levelSizeY];
			level.mapTilesForeground = new int[levelSizeX][levelSizeY];
			level.tileDataForeground = new TileData[levelSizeX][levelSizeY];
			level.tileDataBackground = new TileData[levelSizeX][levelSizeY];

			int maxWidth = Math.min(levelSizeX, oldSizeX);
			int maxHeight = Math.min(levelSizeY, oldSizeY);
			for(int x = 0; x < maxWidth; x++) {
				for(int y = 0; y < maxHeight; y++) {
					level.mapTilesBackground[x][y] = oldBackground[x][y];
					level.mapTilesForeground[x][y] = oldForeground[x][y];
					level.tileDataBackground[x][y] = oldTDBackground[x][y];
					level.tileDataForeground[x][y] = oldTDForeground[x][y];
				}
			}
		});
		
		LevelEditor.addAction("Change Sky Color", (args) -> {
			Color skyColor = JColorChooser.showDialog(this, "Choose Color", new Color(98, 204, 249));
			if(skyColor != null)
				le.level.skyColor = skyColor;
		});
		
		LevelEditor.addAction("Test", (args) -> {
			float oldZoom = Main.zoom;
			Main.changeZoom(1);
			File file;
			
			try {
				file = new File("/files/level.tgl");
				file.deleteOnExit();
				while(!file.exists());
				SaveFile.save(le.level, file.getAbsolutePath());
				TowerGame.hasWon = false;
				Main.frames = 0;
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			TowerGame.main(new String[] {file.getAbsolutePath(), "true"});
			new Thread() {
				{
					setDaemon(true);
				}
				@Override public void run() {
					while(TowerGame.isRunning()){
						try {
							Thread.sleep(16);
						} catch(InterruptedException e) {
							return;
						}
					}
					Main.changeZoom(oldZoom);
				}
			}.start();
		});
		
		LevelEditor.addAction("Zoom In", (args) -> {
			LevelEditorUtils.zoomIn();
		});
		
		LevelEditor.addAction("Zoom Out", (args) -> {
			LevelEditorUtils.zoomOut();
		});
		LevelEditor.addAction("Change Gravity", (args) -> {
			String str;
			if (args.length > 1)
				str = args[1];
			else
				str = JOptionPane.showInputDialog(null, "Gravity:", "Change Gravity", JOptionPane.QUESTION_MESSAGE);
			if(str != null) {
				le.level.gravity = (Double.parseDouble(str) * 0.007);
				if(le.level.gravity == 0)
					le.level.gravity = Double.MIN_VALUE;
			}
		});
	}
}
