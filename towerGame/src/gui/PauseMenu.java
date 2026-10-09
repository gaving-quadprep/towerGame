package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;

import javax.swing.JOptionPane;

import main.Main;
import map.Level;
import save.SaveFile;
import towerGame.TowerGame;
import util.CollisionChecker;

public class PauseMenu extends Timer {
	public static final Color buttonColor = new Color(168,168,168,127);
	public static final Color buttonColor2 = new Color(192,192,192,127);
	private Rectangle buttonArea = new Rectangle(2*Main.scale, 20*Main.scale, 56*Main.scale, 12*Main.scale);
	private boolean saved = false;
	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(0,0,320*Main.scale,240*Main.scale);
		g2.setColor(Color.WHITE);

		super.render(g2, level);
		if (saved) {
			GUI.fontRenderer.drawText(g2, "Saved!", 4*Main.scale, 22*Main.scale);
		} else {
			drawRectHighlightable(g2, buttonArea.x, buttonArea.y, buttonArea.width, buttonArea.height, buttonColor, buttonColor2);
			GUI.fontRenderer.drawText(g2, "Save progress", 4*Main.scale, 22*Main.scale);
		}
	}
	
	@Override
	public void onMouseClick(Point mousePos) {
		if (!saved) {
			if (CollisionChecker.rectContains(buttonArea, mousePos)) {
				File saveFile = FileSystems.getDefault().getPath(Main.progLevelName).toFile();
				try {
					SaveFile.save(TowerGame.gamePanel.level, saveFile);
					saved = true;
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: Failed to save level: "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	@Override
	public void onShown() {
		saved = false;
	}

}
