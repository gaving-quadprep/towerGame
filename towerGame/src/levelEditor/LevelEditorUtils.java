package levelEditor;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import main.Main;
import map.CustomTile;
import map.Level;
import map.Tile;

import static levelEditor.LevelEditor.*;

public abstract class LevelEditorUtils {

	public static int[] getTilePosFromMouse() {
		Point mousePos = new Point(gamePanel.eventHandler.mousePosX, gamePanel.eventHandler.mousePosY);//= MouseInfo.getPointerInfo().getLocation();
		return new int[] { (int) Math.floor((double) (mousePos.x) / Main.tileSize + gamePanel.level.cameraX),
				(int) Math.floor((double) (mousePos.y) / Main.tileSize + gamePanel.level.cameraY) };
	}
	public static double[] getUnroundedTilePosFromMouse() {
		Point mousePos = new Point(gamePanel.eventHandler.mousePosX, gamePanel.eventHandler.mousePosY);//MouseInfo.getPointerInfo().getLocation();
		return new double[] { (double) (mousePos.x) / Main.tileSize + gamePanel.level.cameraX,
				(double) (mousePos.y) / Main.tileSize + gamePanel.level.cameraY };
	}
	public static void addCustomTileToMenu(CustomTile t) {
		if(t.name.equals("")) {
			addButton("tile "+t.id, t.texture, true, customTilePanel);
		}else {
			addButton("tile "+t.id, t.texture, true, t.name, customTilePanel);
		}
		menu.invalidate();
		menu.repaint();
	}
	// very original code made by me
	public static Rectangle autoGetHitbox(BufferedImage image) {
		WritableRaster raster = image.getAlphaRaster();
		int width = raster.getWidth();
		int height = raster.getHeight();
		int left = 0;
		int top = 0;
		int right = width - 1;
		int bottom = height - 1;
		int minRight = width - 1;
		int minBottom = height - 1;

		top:
		for (;top <= bottom; top++){
			for (int x = 0; x < width; x++){
				if (raster.getSample(x, top, 0) != 0){
					minRight = x;
					minBottom = top;
					break top;
				}
			}
		}

		left:
		for (;left < minRight; left++){
			for (int y = height - 1; y > top; y--){
				if (raster.getSample(left, y, 0) != 0){
					minBottom = y;
					break left;
				}
			}
		}

		bottom:
		for (;bottom > minBottom; bottom--){
			for (int x = width - 1; x >= left; x--){
				if (raster.getSample(x, bottom, 0) != 0){
					minRight = x;
					break bottom;
				}
			}
		}

		right:
		for (;right > minRight; right--){
			for (int y = bottom; y >= top; y--){
				if (raster.getSample(right, y, 0) != 0){
					break right;
				}
			}
		}

		return new Rectangle(left, top, right - left + 1, bottom - top + 1);
	}
	public static void clearCustomTiles() {
		for(int i = 0; i < 4096; i++) {
			Tile.tiles[i + 4096] = null;
			Tile.customTiles[i] = null;
		}
		Level level = gamePanel.level;
		for(int y = 0; y < level.sizeY; y++) {
			for(int x = 0; x < level.sizeX; x++) {
				if(level.getTileBackground(x, y) > 4095)
					level.setTileBackground(x, y, 0);
				if(level.getTileForeground(x, y) > 4095)
					level.setTileForeground(x, y, 0);
			}
		}
		if(gamePanel.eventHandler.tileBrush > 4095)
			gamePanel.eventHandler.tileBrush = 0;
		customTilePanel.removeAll();
		menu.invalidate();
		menu.repaint();
	}
	public static void zoomIn() {
		if(Main.zoom <= 4) {
			Main.changeZoom(Main.zoom * 2);
			gamePanel.level.cameraX += Main.width/2;
			gamePanel.level.cameraY += Main.height/2;
		}
	}
	public static void zoomOut() {
		if(Main.tileSize > 3) {
			gamePanel.level.cameraX -= Main.width/2;
			gamePanel.level.cameraY -= Main.height/2;
			Main.changeZoom(Main.zoom / 2);
		}
	}
	public static BufferedImage makeUnindexed(BufferedImage b) {
		BufferedImage newImage = new BufferedImage(b.getWidth(), b.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		newImage.getGraphics().drawImage(b, 0, 0, null);
		return newImage;
	}
	
	

	public static void addMenuItem(JMenu menu, String name, int hk) {
		JMenuItem menuItem=new JMenuItem(name, hk);
		menu.add(menuItem);
		menuItem.addActionListener(gamePanel);
	}
	public static void addButton(String command, Image icon, boolean resizable, JPanel panel) {
		JButton button = new JButton(new ImageIcon(icon));
		button.setPreferredSize(new Dimension(32, 32));
		button.setActionCommand(command);
		button.addActionListener(gamePanel);
		button.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(resizable) {
					JButton btn = (JButton) e.getComponent();
					Dimension size = btn.getSize();
					Insets insets = btn.getInsets();
					size.width -= insets.left + insets.right;
					size.height -= insets.top + insets.bottom;
					if (size.width > size.height) {
						size.width = -1;
					} else {
						size.height = -1;
					}
					Image scaled = icon.getScaledInstance(size.width == 0 ? 1 : size.width, size.height == 0 ? 1 : size.height, java.awt.Image.SCALE_SMOOTH);
					btn.setIcon(new ImageIcon(scaled));
				}
			}
			
		});
		panel.add(button);
	}
	public static void addButton(String command, Image icon, boolean resizable, String tooltip, JPanel panel) {
		JButton button = new JButton(new ImageIcon(icon));
		button.setPreferredSize(new Dimension(32, 32));
		button.setActionCommand(command);
		button.setToolTipText(tooltip);
		button.addActionListener(gamePanel);
		button.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				if(resizable) {
					JButton btn = (JButton) e.getComponent();
					Dimension size = btn.getSize();
					Insets insets = btn.getInsets();
					size.width -= insets.left + insets.right;
					size.width =  size.width < 1 ? -1 : Math.max(4, size.width-4);
					size.height =  size.height < 1 ? -1 : Math.max(4, size.height-4);
					size.height -= insets.top + insets.bottom;
					if (size.width > size.height) {
						size.width = -1;
					} else {
						size.height = -1;
					}
					Image scaled = icon.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
					btn.setIcon(new ImageIcon(scaled));
				}
			}
			
		});
		panel.add(button);
	}
	public static void addButton(String command, String text, JPanel panel) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.addActionListener(gamePanel);
		panel.add(button);
	}
	public static void addButton(String command, String text, String tooltip, JPanel panel) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.setToolTipText(tooltip);
		button.addActionListener(gamePanel);
		panel.add(button);
	}
}
