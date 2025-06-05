package levelEditor.menu;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.panel.PlayerPanel;
import main.Main;
import map.Level;
import save.SaveFile;
import util.Position;
import weapon.Weapon;

@SuppressWarnings("serial")
public class FileMenu extends EditorMenu {
	public FileMenu(LevelEditor le, String name) {
		super(le, name);
		
		LevelEditorUtils.addMenuItem(this, "New", KeyEvent.VK_N);
		LevelEditorUtils.addMenuItem(this, "Save", KeyEvent.VK_S);
		LevelEditorUtils.addMenuItem(this, "Load", KeyEvent.VK_L);
		
		LevelEditor.addAction("Save", (args) -> {
			String path = "/files/level.tgl";
			if(args.length > 1) {
				path = args[1];
			}
			try {
				SaveFile.save(le.level, path);
				Main.downloadSavedFile(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		

		LevelEditor.addAction("Load", (args) -> {
			String path;
			if(args.length > 1) {
				path = args[1];
			} else {
				path = Main.promptFile();
			}
			try {
				le.level.clearSprites();
				SaveFile.load(le.level, path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Main.worldRenderer.level = le.level;
			LevelEditor.customSprites.clear();
			for(Map.Entry<String, BufferedImage> entry : le.level.sprites.entrySet()) {
				LevelEditor.customSprites.put(entry.getKey(), entry.getValue());
			}
			LevelEditor.playerPanel.updateValues(PlayerPanel.ALL);
		});
		
		LevelEditor.addAction("New", (args) -> {
			Position size = LevelEditorUtils.promptCoordinates("Level size");
			if(size != null) {
				le.level = null;
				System.gc();
				le.level = new Level((int) size.x, (int) size.y, true);
				le.playerHealth = 10.0;
				le.playerMana = 15.0;
				le.playerWeapon = Weapon.staff.id;
				LevelEditor.customSprites.clear();
				LevelEditor.playerPanel.updateValues(PlayerPanel.ALL);
			}
		});
	}
}
