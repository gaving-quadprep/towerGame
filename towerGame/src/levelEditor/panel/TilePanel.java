package levelEditor.panel;

import static levelEditor.LevelEditor.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import levelEditor.CheckBoxListener;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.tool.DrawTiles;
import levelEditor.tool.Tool;
import main.Main;
import map.CustomTile;
import map.Tile;
import map.interactable.TileWithData;

@SuppressWarnings("serial")
public class TilePanel extends EditorPanel {

	BufferedImage addTileImage;
	CustomTile createdTile;
	public JPanel innerCustomTilePanel;
	CheckBoxListener cb1;
	

	public static void addCustomTileToMenu(CustomTile t, JPanel customTilePanel) {
		JButton button;
		if(t.name.equals("")) {
			button = LevelEditorUtils.addButton("tile;"+t.id, t.texture, true, customTilePanel);
		} else {
			button = LevelEditorUtils.addButton("tile;"+t.id, t.texture, true, t.name, customTilePanel);
		}
		
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if(SwingUtilities.isRightMouseButton(me)) {
					JPopupMenu deleteMenu = new JPopupMenu();
					LevelEditorUtils.addMenuItem(deleteMenu, "Delete", "DeleteTile;"+t.id);
					deleteMenu.show(me.getComponent(), me.getX(), me.getY());
				}
			}
		});
		
		menu.invalidate();
		menu.repaint();
	}
	
	public TilePanel(LevelEditor le) {
		super(le);
		
		this.setLayout(new GridLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		this.add(tabbedPane);
		//tabbedPane.setSize(getWidth(), getHeight());
		//tabbedPane.setMaximumSize(new Dimension(getWidth(), getHeight()));
		//tabbedPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
		JPanel defaultTilePanel = new JPanel(), customTilePanel = new JPanel();
		tabbedPane.add("Default", defaultTilePanel);
		tabbedPane.add("Custom", customTilePanel);
		
		BufferedImage tilemap = LevelEditorUtils.readImage("/sprites/tilemap.png");
		defaultTilePanel.setLayout(new GridLayout(0, 4));
		int texId = 0;
		for (int i=0; i<Tile.maxTile+1; i++) {
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			texId = Tile.tiles[i].getTextureId();
			int frameX = (texId % 16) * 16;
			int frameY = (texId / 16) * 16;
			g2.drawImage(tilemap, 0, 0, 16, 16,frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
			LevelEditorUtils.addButton("tile;"+String.valueOf(i), img, true, defaultTilePanel);
		}
		customTilePanel.setLayout(new BorderLayout());
		//customTilePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		innerCustomTilePanel = new JPanel();
		innerCustomTilePanel.setBorder(BorderFactory.createTitledBorder("Created Tiles"));
		innerCustomTilePanel.setPreferredSize(new Dimension(190, 100));
		innerCustomTilePanel.setVisible(true);
		customTilePanel.add(innerCustomTilePanel, BorderLayout.CENTER);
		JPanel addTile = new JPanel();
		addTile.setPreferredSize(new Dimension(200, 225));
		//addTile.setMinimumSize(new Dimension(150, 200));
		//addTile.setLayout(new BoxLayout(addTile, BoxLayout.Y_AXIS));
		LevelEditorUtils.addButton("Choose Tile Image", addTile);
		
		JPanel checkboxPanel = new JPanel();
		checkboxPanel.setLayout(new GridLayout(0, 1));
		JCheckBox b1 = new JCheckBox("Tile collision", true);
		JCheckBox b2 = new JCheckBox("Does damage");
		JCheckBox b3 = new JCheckBox("Auto detect custom hitbox");
		checkboxPanel.add(b1);
		checkboxPanel.add(b2);
		checkboxPanel.add(b3);
		CheckBoxListener cbl = new CheckBoxListener(new JCheckBox[] {b1, b2, b3});
		addTile.add(checkboxPanel);
		
		JTextField nameField;
		addTile.add(new JLabel("Name (optional)"));
		addTile.add(nameField = new JTextField(12));
		
		LevelEditorUtils.addButton("Create Tile", addTile);
		customTilePanel.add(addTile, BorderLayout.SOUTH);
		
		LevelEditor.addAction("tile", (args) -> {
			if(args.length < 2) 
				return;
			int tile = Integer.valueOf(args[1]);
			le.eventHandler.tileBrush = tile;
			if (!(le.tool instanceof DrawTiles))
				le.tool = Tool.drawTiles;
			if (Tile.tiles[tile] instanceof TileWithData) {
				LevelEditor.placeTileData = ((TileWithData)Tile.tiles[tile]).promptTileData();
			}
		});
		
		LevelEditor.addAction("Choose Tile Image", (args) -> {
			BufferedImage image;
			try {
				image = ImageIO.read(new File(Main.promptFile()));
				addTileImage = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
				addTileImage.getGraphics().drawImage(LevelEditorUtils.makeUnindexed(image), 0, 0, 16, 16, null);
				
			} catch (Exception e) {
				e.printStackTrace();
				// Main.hamburger();
			}
		});
		
		LevelEditor.addAction("Create Tile", (args) -> {
			if(addTileImage != null) {
				if(cbl.selected[2]) {
					Rectangle hitbox = LevelEditorUtils.autoGetHitbox(addTileImage);
					createdTile = new CustomTile(addTileImage, cbl.selected[0], cbl.selected[1], hitbox);
				}else {
					createdTile = new CustomTile(addTileImage, cbl.selected[0], cbl.selected[1]);
				}
				createdTile.name = nameField.getText();
				addCustomTileToMenu(createdTile, innerCustomTilePanel);
			} else {
				JOptionPane.showMessageDialog(null, "You need to upload a tile image", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		LevelEditor.addAction("DeleteTile", (args) -> {
			if(args.length < 2)
				return;
			int tileId = Integer.valueOf(args[1]);
			
			for (Component c : innerCustomTilePanel.getComponents()) {
				if (c instanceof JButton) {
					JButton b = (JButton)c;
					if (b.getActionCommand().equals("tile;"+tileId))
						innerCustomTilePanel.remove(b);
				}
			}
			
			if(le.eventHandler.tileBrush == tileId)
				le.eventHandler.tileBrush = 0;

			for(int y = 0; y < le.level.sizeY; y++) {
				for(int x = 0; x < le.level.sizeX; x++) {
					if(le.level.getTileBackground(x, y) == tileId)
						le.level.setTileBackground(x, y, 0);
					if(le.level.getTileForeground(x, y) == tileId)
						le.level.setTileForeground(x, y, 0);
				}
			}
			
			Tile.tiles[tileId] = null;
			Tile.customTiles[tileId - 4096] = null;
			
			innerCustomTilePanel.repaint();
		});
		
	}

	public String getName() {
		return "Tile";
	}
	
	public String getIcon() {
		return "/sprites/levelEditor/DrawTiles.png";
	}

}
