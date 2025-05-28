package levelEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Main;
import map.CustomTile;
import map.Level;
import map.Tile;
import util.PixelPosition;
import util.Position;

import static levelEditor.LevelEditor.*;

public abstract class LevelEditorUtils {
	
	@SuppressWarnings("serial")
	public static class XYInput extends JPanel {
		public JTextField xInput;
		public JTextField yInput;
		public XYInput(String title) {
			super(new BorderLayout(5,5));
			add(new JLabel(title), BorderLayout.PAGE_START);
	        JPanel labels = new JPanel(new GridLayout(0, 1, 2, 2));
	        labels.add(new JLabel("X: ", SwingConstants.TRAILING));
	        labels.add(new JLabel("Y: ", SwingConstants.TRAILING));
	        add(labels, BorderLayout.LINE_START);

	        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	        xInput = new JTextField();
	        controls.add(xInput);
	        yInput = new JTextField();
	        controls.add(yInput);
	        add(controls, BorderLayout.CENTER);
		}
	}
	
	
	public static int[] getTilePosFromMouse() {
		PixelPosition mousePos = new PixelPosition(gamePanel.eventHandler.mousePosX, gamePanel.eventHandler.mousePosY);
		return new int[] { (int) Math.floor((double) (mousePos.x) / Main.tileSize + gamePanel.level.cameraX),
				(int) Math.floor((double) (mousePos.y) / Main.tileSize + gamePanel.level.cameraY) };
	}
	public static Position getUnroundedTilePosFromMouse() {
		PixelPosition mousePos = new PixelPosition(gamePanel.eventHandler.mousePosX, gamePanel.eventHandler.mousePosY);
		return new Position((double) (mousePos.x) / Main.tileSize + gamePanel.level.cameraX,
				(double) (mousePos.y) / Main.tileSize + gamePanel.level.cameraY);
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
		JPanel tp = LevelEditor.tilePanel.innerCustomTilePanel;
		tp.removeAll();
		//menu.invalidate();
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
	
	public static void addMenuItem(JComponent menu, String name, int hk) {
		JMenuItem menuItem = new JMenuItem(name, hk);
		menu.add(menuItem);
		menuItem.addActionListener(gamePanel);
	}
	
	public static void addMenuItem(JComponent menu, String name, String command) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setActionCommand(command);
		menu.add(menuItem);
		menuItem.addActionListener(gamePanel);
	}

	public static void addCheckBoxMenuItem(JMenu menu, String name, String command, boolean selected) {
		JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(name);
		menuItem.setActionCommand(command);
		menuItem.setSelected(selected);
		menu.add(menuItem);
		menuItem.addActionListener(gamePanel);
	}
	public static void addRadioButtonMenuItem(JMenu menu, String name, String command, ButtonGroup group, boolean selected) {
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(name);
		menuItem.setActionCommand(command);
		menuItem.setSelected(selected);
		group.add(menuItem);
		menu.add(menuItem);
		menuItem.addActionListener(gamePanel);
	}
	public static JButton addButton(String command, Image icon, boolean resizable, String tooltip, JPanel panel) {
		JButton button = new JButton(new ImageIcon(icon));
		Dimension preferredSize = button.getPreferredSize();
		button.setPreferredSize(new Dimension(Math.max(48, preferredSize.width), Math.max(32, preferredSize.height)));
		button.setActionCommand(command);
		if (tooltip != null)
			button.setToolTipText(tooltip);
		button.addActionListener(gamePanel);

		if(resizable) {	
			button.addComponentListener(new ComponentAdapter() {
				
				@Override
				public void componentResized(ComponentEvent e) {
					JButton btn = (JButton) e.getComponent();
					Dimension size = btn.getSize();
					Insets insets = btn.getInsets();
					size.width -= insets.left + insets.right;
					size.height -= insets.top + insets.bottom;
					size.width =  size.width < 1 ? -1 : Math.max(8, size.width);
					size.height =  size.height < 1 ? -1 : Math.max(8, size.height);
					if (size.width > size.height) {
						size.width = -1;
					} else {
						size.height = -1;
					}
					Image scaled = icon.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
					btn.setIcon(new ImageIcon(scaled));
				}
				
			});
		}
		
		panel.add(button);
		return button;
	}
	
	public static JButton addButton(String command, Image icon, boolean resizable, JPanel panel) {
		return addButton(command, icon, resizable, null, panel);
	}
	
	public static JButton addButton(String command, String text, JPanel panel) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.addActionListener(gamePanel);
		panel.add(button);
		return button;
	}
	
	public static JButton addButton(String command, String text, String tooltip, JPanel panel) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.setToolTipText(tooltip);
		button.addActionListener(gamePanel);
		panel.add(button);
		return button;
	}
	
	public static JButton addButton(String text, JPanel panel) {
		return addButton(text, text, panel);
	}
	
	public static void addSpacer(JPanel panel, boolean yAxis, int size) {
		Component rigidArea;
		if(yAxis)
			rigidArea = Box.createRigidArea(new Dimension(0, size));
		else
			rigidArea = Box.createRigidArea(new Dimension(size, 0));
		panel.add(rigidArea);
	}
	
	public static BufferedImage readImage(String path) {
		try {
			return ImageIO.read(LevelEditor.class.getResourceAsStream(path));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Position promptCoordinates(String title) {
		// stolen from https://stackoverflow.com/questions/10773132/how-to-unfocus-a-jtextfield/10773412#10773412
		JFrame frame = new JFrame(title);
		
		XYInput xyi = new XYInput(title + ":");

        int option = JOptionPane.showConfirmDialog(frame, xyi, title, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
        	return new Position(Double.parseDouble(xyi.xInput.getText()), Double.parseDouble(xyi.yInput.getText()));
        }
        
        return null;
	}
	public static void addWithLabel(JPanel panel, Component c, String labelText) {
		JLabel label = new JLabel(labelText);
		label.setLabelFor(c);
		panel.add(label);
		panel.add(c);

	}
	
}
