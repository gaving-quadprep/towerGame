package levelEditor;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import map.Tile;
import map.interactable.TileWithData;

public class TilePanel extends EditorPanel {

	public TilePanel(LevelEditor le) {
		super(le);
	}
	
	public void initialize() {
		this.setLayout(new GridLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		this.add(tabbedPane);
		//tabbedPane.setSize(getWidth(), getHeight());
		//tabbedPane.setMaximumSize(new Dimension(getWidth(), getHeight()));
		//tabbedPane.setPreferredSize(new Dimension(getWidth(), getHeight()));
		JPanel defaultTilePanel = new JPanel(), customTilePanel = new JPanel();
		tabbedPane.add("Default", defaultTilePanel);
		tabbedPane.add("Custom", customTilePanel);
		
		BufferedImage tilemap;
		try {
			tilemap = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/tilemap.png"));
		} catch (IOException e) {
			tilemap = null;
			e.printStackTrace();
		}
		defaultTilePanel.setLayout(new GridLayout(0, 3));
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
		customTilePanel.setLayout(new BoxLayout(customTilePanel, BoxLayout.Y_AXIS));
		//customTilePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		JPanel innerCustomTilePanel = new JPanel();
		innerCustomTilePanel.setBorder(BorderFactory.createTitledBorder("Created Tiles"));
		innerCustomTilePanel.setPreferredSize(new Dimension(190, 100));
		innerCustomTilePanel.setVisible(true);
		customTilePanel.add(innerCustomTilePanel);
		JPanel addTile = new JPanel();
		//addTile.setLayout(new BoxLayout(addTile, BoxLayout.Y_AXIS));
		LevelEditorUtils.addButton("addtile choosefile", "Choose Tile Image", addTile);
		JCheckBox b1 = new JCheckBox("Tile collision", true);
		JCheckBox b2 = new JCheckBox("Does damage");
		JCheckBox b3 = new JCheckBox("Auto detect custom hitbox");
		addTile.add(b1);
		addTile.add(b2);
		addTile.add(b3);
		CheckBoxListener cbl = new CheckBoxListener(new JCheckBox[] {b1, b2, b3});
		
		JTextField nameField;
		addTile.add(new JLabel("Name (optional)"));
		addTile.add(nameField = new JTextField(12));
		
		LevelEditorUtils.addButton("addtile submit", "Create Tile", addTile);
		customTilePanel.add(addTile);
		
		LevelEditor.addAction("tile", (args) -> {
			if(args.length < 2) 
				return;
			int tile = Integer.valueOf(args[1]);
			LevelEditor.gamePanel.eventHandler.tileBrush = tile;
			if (LevelEditor.gamePanel.tool != Tool.FILLTILES)
				LevelEditor.gamePanel.tool = Tool.DRAWTILES;
			if (Tile.tiles[tile] instanceof TileWithData) {
				LevelEditor.placeTileData = ((TileWithData)Tile.tiles[tile]).promptTileData();
			}
		});
		
		// TODO Auto-generated constructor stub
		System.out.print("tbp width:    ");
		System.out.println(tabbedPane.getWidth());
		System.out.print("dtp width:    ");
		System.out.println(defaultTilePanel.getWidth());
		System.out.print("tp width:     ");
		System.out.println(this.getWidth());
		System.out.print("parent width: ");
		System.out.println(getParent().getWidth());
		
	}

}
