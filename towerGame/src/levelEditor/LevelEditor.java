package levelEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.Decoration;
import entity.Entity;
import entity.FallingTile;
import entity.FireEnemy;
import entity.FlameDemon;
import entity.FloatingPlatform;
import entity.ManaOrb;
import entity.PuddleMonster;
import entity.Thing;
import entity.ZombieKnight;
import gui.GUI;
import item.ItemWeapon;
import main.Main;
import main.Renderer;
import map.CustomTile;
import map.Level;
import map.Tile;
import map.interactable.ChestTile;
import map.interactable.EntityFactory;
import map.interactable.TileData;
import map.interactable.TileWithData;
import save.SaveFile;
import towerGame.TowerGame;
import util.CollisionChecker;
import weapon.Weapon;

@SuppressWarnings("serial")
public class LevelEditor extends JPanel implements Runnable, ActionListener {
	Thread gameThread;
	public JFrame frame;
	public static JFrame menu;
	public static LevelEditor gamePanel;
	LEEventHandler eventHandler = new LEEventHandler(frame);
	public Tool tool = Tool.DRAWTILES;
	public int drawEntity;
	protected boolean debug=false;
	double currentTime, remainingTime, finishedTime;
	public Level level = new Level(Main.width, Main.height, true);
	{
		Main.worldRenderer.level = level;
	}
	static boolean testing;
	static JMenuBar menuBar;
	static JTextField nameField;
	static BufferedImage iconFireEnemy, iconFireEnemyBlue, iconThing, iconManaOrb, iconPlatform, iconFlameDemon, iconPuddleMonster, iconZombieKnight, addTileImage, fillTool;
	static CustomTile createdTile;
	static CheckBoxListener cbl;
	static JPanel customTilePanel;
	static Entity selectedEntity;
	static Decoration placeableDecoration;
	static TileData placeTileData;
	public static double playerHealth = 10.0;
	public static double playerMana = 15.0;
	public static int playerWeapon = Weapon.staff.id;
	
	public LevelEditor() {
		this.addKeyListener(eventHandler);
		this.addMouseListener(eventHandler);
		this.addMouseMotionListener(eventHandler);
		this.setPreferredSize(new Dimension(320*Main.scale,240*Main.scale));
		this.setDoubleBuffered(true);
		this.setBackground(Color.black);
	}
	private void writeObject(ObjectOutputStream oos) throws IOException {
		throw new NotSerializableException();
	}
	public int[] getTilePosFromMouse() {
		Point mousePos = new Point(eventHandler.mousePosX, eventHandler.mousePosY);//= MouseInfo.getPointerInfo().getLocation();
		return new int[] { (int) Math.floor((double) (mousePos.x) / Main.tileSize + level.cameraX),
				(int) Math.floor((double) (mousePos.y) / Main.tileSize + level.cameraY) };
	}
	public double[] getUnroundedTilePosFromMouse() {
		Point mousePos = new Point(eventHandler.mousePosX, eventHandler.mousePosY);//MouseInfo.getPointerInfo().getLocation();
		return new double[] { (double) (mousePos.x) / Main.tileSize + level.cameraX,
				(double) (mousePos.y) / Main.tileSize + level.cameraY };
	}
	public static void addCustomTileToMenu(CustomTile t) {
		if(t.name.equals("")) {
			addButton("tile "+t.id, t.texture, customTilePanel);
		}else {
			addButton("tile "+t.id, t.texture, t.name, customTilePanel);
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
	public void clearCustomTiles() {
		for(int i = 0; i < 4096; i++) {
			Tile.tiles[i + 4096] = null;
			Tile.customTiles[i] = null;
		}
		for(int y = 0; y < level.sizeY; y++) {
			for(int x = 0; x < level.sizeX; x++) {
				if(level.getTileBackground(x, y) > 4095)
					level.setTileBackground(x, y, 0);
				if(level.getTileForeground(x, y) > 4095)
					level.setTileForeground(x, y, 0);
			}
		}
		if(eventHandler.tileBrush > 4095)
			eventHandler.tileBrush = 0;
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
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		if(Renderer.currentGraphicsConfiguration == null)
			Renderer.currentGraphicsConfiguration = g2.getDeviceConfiguration();
		Main.worldRenderer.setGraphics(g2);
		g2.setColor(level.skyColor);
		g2.fillRect(0, 0, 320*Main.scale, 240*Main.scale);
		if(eventHandler.downPressed) {
			level.cameraY += 0.140625 / Main.zoom;
		}
		if(eventHandler.upPressed) {
			level.cameraY -= 0.140625 / Main.zoom;
		}
		if(eventHandler.leftPressed) {
			level.cameraX -= 0.140625 / Main.zoom;
		}
		if(eventHandler.rightPressed) {
			level.cameraX += 0.140625 / Main.zoom;
		}
		try {
			if(eventHandler.editBackground) {
				level.renderBackgroundOnly(Main.worldRenderer);
			}else {
				level.render(Main.worldRenderer);
			}
			int[] positions = getTilePosFromMouse();
			double[] positions2 = getUnroundedTilePosFromMouse();
			Main.worldRenderer.drawRect(positions[0], positions[0]+1, positions[1], positions[1]+1,  new Color(0, 0, 0, 96));
			
			switch(tool) {
			case DRAWTILES:
			case FILLTILES:
				if(eventHandler.tileBrush < 4096) {
					int frameX = (Tile.tiles[eventHandler.tileBrush].getTextureId() % 16) * 16;
					int frameY = (Tile.tiles[eventHandler.tileBrush].getTextureId() / 16) * 16;
					Main.worldRenderer.drawTiledImage(level.tilemap, positions2[0]-0.5, positions2[1]-0.5, 1, 1, frameX, frameY, frameX+16, frameY+16);
				}else {
					Main.worldRenderer.drawImage(((CustomTile)Tile.tiles[eventHandler.tileBrush]).texture, positions2[0]-0.5, positions2[1]-0.5, 1, 1);
				}
				if(tool == Tool.FILLTILES)
					Main.worldRenderer.drawImage(fillTool, positions2[0] - 1, positions2[1] - 1, 1, 1);
				break;
				
			case ADDENTITY:
				BufferedImage entitysprite;
				int sizeX = 1, sizeY = 1;
				switch(drawEntity) {
				case 0:
					entitysprite=iconFireEnemy;
					break;
				case 1:
					entitysprite=iconFireEnemyBlue;
					break;
				case 2:
					entitysprite=iconThing;
					break;
				case 3:
					entitysprite=iconManaOrb;
					break;
				case 4:
					entitysprite=iconPlatform;
					break;
				case 5:
					entitysprite=iconFlameDemon;
					sizeX = 2;
					sizeY = 2;
					break;
				case 6:
					entitysprite=iconPuddleMonster;
					break;
				case 7:
					entitysprite=iconZombieKnight;
					break;
				default:
					entitysprite=null;
				}
				Main.worldRenderer.drawImage(entitysprite, positions2[0]-0.5, positions2[1]-0.5, sizeX, sizeY);
				break;
			case PLACEDECORATION:
				if(placeableDecoration != null) {
					Main.worldRenderer.drawImage(placeableDecoration.sprite, positions2[0]-0.5, positions2[1]-0.5, placeableDecoration.imageSizeX/16.0, placeableDecoration.imageSizeY/16.0);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		if(eventHandler.debugPressed) {
			g2.setColor(new Color(128, 0, 0, 192));
			GUI.fontRenderer.drawText(g2, "Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M", 10, 10);
		}
		g2.setColor(new Color(0, 0, 0, 192));
		int[] positions = getTilePosFromMouse();
		g2.drawString("X " + String.valueOf(positions[0]), 10, (Main.scale*240) - 20);
		g2.drawString("Y " + String.valueOf(positions[1]), 10, (Main.scale*240) - 10);
		
		g2.dispose();
	};
	public void actionPerformed(ActionEvent event) {
		try {
			JFileChooser fc = new JFileChooser();
			String ac = event.getActionCommand();
			if(ac=="Save") {
				fc.setFileFilter(new FileNameExtensionFilter(
						"TowerQuest Level", "tgl"));
				fc.setSelectedFile(new File("level.tgl"));
				int returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path =fc.getSelectedFile().getPath();
					if (!path .endsWith(".tgl"))
						path += ".tgl";
					SaveFile.save(level, path);
				}
			}
			if(ac=="Load") {
				fc.setFileFilter(new FileNameExtensionFilter(
						"TowerQuest Level", "tgl"));
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveFile.load(level, fc.getSelectedFile().getPath());
					Main.worldRenderer.level = level;
				}
			}
			if(ac=="Add Entity") {
				String userInput = JOptionPane.showInputDialog(null, "Entity type", "Add Entity", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					Entity entity;
					if(userInput.equals("Decoration") && placeableDecoration != null) {
						entity = placeableDecoration;
					}else {
						entity = Entity.entityRegistry.createByName(userInput, new Class[] {Level.class}, new Object[] {level});
					}
					if(entity!=null) {
						userInput = JOptionPane.showInputDialog(null, "Entity posX", "Add Entity", JOptionPane.QUESTION_MESSAGE);
						double x=Double.parseDouble(userInput);
						userInput = JOptionPane.showInputDialog(null, "Entity posY", "Add Entity", JOptionPane.QUESTION_MESSAGE);
						double y=Double.parseDouble(userInput);
						entity.setPosition(x,y);
						level.addEntity(entity);
						
					}else {
						JOptionPane.showMessageDialog(null, "Invalid entity type", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			if(ac=="Remove Entity") {
				if(level.entities.size() > 0) {
					Object[] possibleValues = level.entities.toArray();
	
					Object en = JOptionPane.showInputDialog(null,
								 "Choose an entity", "Remove Entity",
								 JOptionPane.INFORMATION_MESSAGE, null,
								 possibleValues, possibleValues[0]);
					level.entities.remove(en);
				}else {
					JOptionPane.showMessageDialog(null, "No entities to remove", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(ac=="New") {
				String userInput = JOptionPane.showInputDialog(null, "Level size X", "New Level", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					int levelSizeX=Integer.parseInt(userInput);
					userInput = JOptionPane.showInputDialog(null, "Level size Y", "New Level", JOptionPane.QUESTION_MESSAGE);
					int levelSizeY=Integer.parseInt(userInput);
					level = null;
					System.gc();
					level = new Level(levelSizeX, levelSizeY,true);
					for(int y=0;y<level.sizeY;y++) {
						for(int x=0;x<level.sizeX;x++) {
							level.setTileBackground(x,y,0);
							level.setTileForeground(x,y,0);
						}
					}
					playerHealth = 10.0;
					playerMana = 15.0;
					playerWeapon = Weapon.staff.id;
				}
			}
			
			if(ac=="Change Sky Color") {
				level.skyColor = JColorChooser.showDialog(this, "Choose Color", new Color(98,204,249));
			}
			if(ac=="Change Player Start") {
				String userInput = JOptionPane.showInputDialog(null, "Player start X", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					level.playerStartX=Double.parseDouble(userInput);
					userInput = JOptionPane.showInputDialog(null, "Player start Y", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
					level.playerStartY=Double.parseDouble(userInput);
				}
			}
			if(ac=="Change Player Health") {
				String userInput = JOptionPane.showInputDialog(null, "Player health:", "Change Player Health", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					playerHealth = Double.parseDouble(userInput);
				}
			}
			if(ac=="Change Player Mana") {
				String userInput = JOptionPane.showInputDialog(null, "Player mana:", "Change Player Mana", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					playerMana = Double.parseDouble(userInput);
				}
			}
			if(ac=="Change Player Weapon") {
				String[] possibleValues = new String[] {"Staff", "Level 2 Staff", "Level 3 Staff", "Shield", "Sword", "Dagger", "Pickaxe"};
				
				String result = (String) JOptionPane.showInputDialog(null,
							 "Choose an weapon", "Change Player Weapon",
							 JOptionPane.INFORMATION_MESSAGE, null,
							 possibleValues, possibleValues[0]);
				if(result == null) 
					return;
				switch(result) {
				case "Staff":
					playerWeapon = Weapon.staff.id; 
					break;
				case "Level 2 Staff":
					playerWeapon = Weapon.staffUpgraded.id; 
					break;
				case "Level 3 Staff":
					playerWeapon = Weapon.staffUpgraded2.id; 
					break;
				case "Shield":
					playerWeapon = Weapon.shield.id; 
					break;
				case "Sword":
					playerWeapon = Weapon.sword.id; 
					break;
				case "Dagger":
					playerWeapon = Weapon.dagger.id; 
					break;
				case "Pickaxe":
					playerWeapon = Weapon.pickaxe.id; 
					break;
				}
			}
			if(ac=="Test") {
				float oldZoom = Main.zoom;
				Main.changeZoom(1);
				File file = File.createTempFile("temp", null);
				file.deleteOnExit();
				while(!file.exists());
				SaveFile.save(level, file.getAbsolutePath());
				TowerGame.hasWon = false;
				Main.frames = 0;
				
				TowerGame.main(new String[] {file.getAbsolutePath(), "true"});
				new Thread() {
					{
						setDaemon(true);
					}
					@Override public void run() {
						while(TowerGame.isRunning()){
							try {
								Thread.sleep(16);
							}catch(InterruptedException e) {
								return;
							}
						}
						Main.changeZoom(oldZoom);
					}
				}.start();
			}
			if(ac=="Zoom In") {
				zoomIn();
			}
			if(ac=="Zoom Out") {
				zoomOut();
			}
			if((ac.split(" ")[0]).equals("tile")) {
				eventHandler.tileBrush = Integer.valueOf(ac.split(" ")[1]);
				if(gamePanel.tool != Tool.FILLTILES) gamePanel.tool = Tool.DRAWTILES;
				if(eventHandler.tileBrush == Tile.crate.id || eventHandler.tileBrush == Tile.chest.id) {
					String[] possibleValues = new String[] {"No Item", "Shield"};
					
					String result = (String) JOptionPane.showInputDialog(null,
								 "Choose an item", "Item Inside",
								 JOptionPane.INFORMATION_MESSAGE, null,
								 possibleValues, possibleValues[0]);
					if(result == "Shield") {
						placeTileData = new ChestTile.CustomTileData(new ItemWeapon(Weapon.shield.id));
					}else {
						placeTileData = null;
					}
				}else if (eventHandler.tileBrush == Tile.entitySpawner.id) {
					String[] possibleValues = new String[] {"Fire Enemy", "Blue Fire Enemy", "Thing", "Puddle Monster", "Zombie Knight", "Falling Boulder", "Mana Orb"};
					
					String result = (String) JOptionPane.showInputDialog(null,
								 "Choose an entity", "Entity spawned",
								 JOptionPane.INFORMATION_MESSAGE, null,
								 possibleValues, possibleValues[2]);
					Entity e;
					if(result == null) {
						placeTileData = null;
						return;
					}
					switch(result) {
					case "Fire Enemy":
					case "Blue Fire Enemy":
						e = new FireEnemy(level, result.equals("Blue Fire Enemy"));
						break;
					case "Thing":
						e = new Thing(level);
						break;
					case "Puddle Monster":
						e = new PuddleMonster(level);
						break;
					case "Zombie Knight":
						e = new ZombieKnight(level);
						break;
					case "Falling Boulder":
						e = new FallingTile(level, Tile.boulder.id);
						break;
					case "Mana Orb":
						e = new ManaOrb(level);
						break;
					default:
						placeTileData = null;
						return;
					}
					placeTileData = new EntityFactory.CustomTileData(e);

					String userInput = JOptionPane.showInputDialog(null, "Delay between spawning entities (in seconds)", "Delay", JOptionPane.QUESTION_MESSAGE);
					if(userInput!=null) {
						((EntityFactory.CustomTileData)placeTileData).setDelay(Math.max((int)(60.0d * Double.parseDouble(userInput)), 1));
					}
				}else {
					placeTileData = null;
				}
			}
			if((ac.split(" ")[0]).equals("SelectEntity")) {
				gamePanel.drawEntity = Integer.valueOf(ac.split(" ")[1]);
				gamePanel.tool = Tool.ADDENTITY;
			}
			if((ac.split(" ")[0]).equals("Tool")) {
				gamePanel.tool = Tool.fromNumber(Integer.valueOf(ac.split(" ")[1]));
			}
			if(ac=="addtile choosefile") {
				fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					addTileImage = ImageIO.read(new File(fc.getSelectedFile().getPath()));
					BufferedImage newImage=new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
					newImage.getGraphics().drawImage(addTileImage, 0, 0, 16, 16, null);
					addTileImage = newImage;
				}
			}
			if(ac=="addtile submit") {
				if(addTileImage != null) {
					if(cbl.selected[2]) {
						Rectangle hitbox = autoGetHitbox(addTileImage);
						createdTile = new CustomTile(addTileImage, cbl.selected[0], cbl.selected[1], hitbox);
					}else {
						createdTile = new CustomTile(addTileImage, cbl.selected[0], cbl.selected[1]);
					}
					createdTile.name = nameField.getText();
					LevelEditor.addCustomTileToMenu(createdTile);
				}else {
					JOptionPane.showMessageDialog(null, "You need to upload a tile image", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(ac=="Clear Custom Tiles") {
				clearCustomTiles();
			}

			if(ac=="AddDecoration") {
				fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					BufferedImage decorationImage = ImageIO.read(new File(fc.getSelectedFile().getPath()));
					placeableDecoration = new Decoration(level, decorationImage);
					tool = Tool.PLACEDECORATION;
				}
			}
			if(ac=="Change Gravity (may not work properly)") {
				String userInput = JOptionPane.showInputDialog(null, "Gravity:", "Change Gravity", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					level.gravity = (Double.parseDouble(userInput) / 60);
					if(level.gravity == 0)
						level.gravity = 0.000000000000001;
				}
			}
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	public void startGameThread() {
		gameThread=new Thread(this);
		gameThread.start();
	};
	
	public void run() {
		double drawInterval=1000000000/60;
		int frames=0;
		
		while (gameThread!=null) {
			currentTime=System.nanoTime();
			double nextDrawTime=System.nanoTime()+drawInterval;
			//System.out.println("It's running")
			if(eventHandler.mouse1Clicked) {
				int mx, my;
				Rectangle mp;
				switch(tool) {
				case ADDENTITY:
					Entity e;
					switch(drawEntity) {
					case 0:
						e = new FireEnemy(level, false);
						break;
					case 1:
						e = new FireEnemy(level, true);
						break;
					case 2:
						e = new Thing(level);
						break;
					case 3:
						e = new ManaOrb(level);
						break;
					case 4:
						e = new FloatingPlatform(level);
						break;
					case 5:
						e = new FlameDemon(level);
						break;
					case 6:
						e = new PuddleMonster(level);
						break;
					case 7:
						e = new ZombieKnight(level);
						break;
					default:
						e = null;
					}
					if(eventHandler.shiftPressed) {
						double[] positions = getUnroundedTilePosFromMouse();
						e.setPosition(positions[0]-0.5,positions[1]-0.5);
					}else {
						int[] positions = getTilePosFromMouse();
						e.setPosition(positions[0],positions[1]);
					}
					level.addEntity(e);
					break;
				case MOVEENTITY:
					mp = new Rectangle(7,7,2,2);
					double[] positions1 = getUnroundedTilePosFromMouse();
					for (int i = level.entities.size(); i-- > 0;) { // get the one on top
						Entity e2 = level.entities.get(i);
						if(CollisionChecker.checkHitboxes(mp, e2.hitbox, positions1[0], positions1[1], e2.x, e2.y)) {
							selectedEntity = e2;
							// TODO: move entity somehow
							break;
						}
					}
					break;
				case REMOVEENTITY:
					mp = new Rectangle(0,0,2,2);
					double[] positions = getUnroundedTilePosFromMouse();
				
					for (int i = level.entities.size(); i-- > 0;) { // Remove the one on top
						Entity e2 = level.entities.get(i);
						if(CollisionChecker.checkHitboxes(mp, e2.hitbox, positions[0], positions[1], e2.x, e2.y)) {
							e2.markedForRemoval = true;
							break;
						}
					}
					break;
				case FILLTILES:
					int[] positions2 = getTilePosFromMouse();
					mx = positions2[0];
					my = positions2[1];
					if(eventHandler.editBackground) {
						level.floodFill(mx, my, eventHandler.tileBrush, false);
					}else {
						level.floodFill(mx, my, eventHandler.tileBrush, true);
						
					}
					break;
				case PLACEDECORATION:
					if(placeableDecoration != null) {
						double[] positions3 = getUnroundedTilePosFromMouse();
						Decoration decoration;
						decoration = (Decoration) placeableDecoration.clone();
						decoration.setPosition(positions3[0]-0.5,positions3[1]-0.5);
						level.addEntity(decoration);
					}
					break;
				}
			}
			if(eventHandler.mouse1Pressed) {
				switch(tool) {
				case DRAWTILES:
					int[] positions = getTilePosFromMouse();
					if(eventHandler.editBackground) {
						level.setTileBackground(positions[0], positions[1], eventHandler.tileBrush);
						if(placeTileData != null)
							level.setTileDataBackground(positions[0], positions[1], placeTileData);
					}else {
						level.setTileForeground(positions[0], positions[1], eventHandler.tileBrush);
						if(placeTileData != null)
							level.setTileDataForeground(positions[0], positions[1], placeTileData);
					}
					break;
				}
			}
			if(eventHandler.mouse2Pressed) {
				switch(tool) {
				case DRAWTILES:
				case FILLTILES:
					int[] positions = getTilePosFromMouse();
					if(eventHandler.editBackground) {
						eventHandler.tileBrush=level.getTileBackground(positions[0], positions[1]);
					}else {
						if((eventHandler.tileBrush=level.getTileForeground(positions[0], positions[1]))==0) {
							eventHandler.tileBrush=level.getTileBackground(positions[0], positions[1]);
						}
					}
					if(Tile.tiles[eventHandler.tileBrush] instanceof TileWithData) {
						if(eventHandler.editBackground) {
							placeTileData = level.getTileDataBackground(positions[0], positions[1]);
						}else {
							if(level.getTileForeground(positions[0], positions[1])==0) {
								placeTileData = level.getTileDataBackground(positions[0], positions[1]);
							}else {
								placeTileData = level.getTileDataForeground(positions[0], positions[1]);
							}
						}
					}else {
						placeTileData = null;
					}
				}
			}
			repaint();
			if(level!=null) {
				try {
					level.update();
				} catch (Exception e) {
					
				}
			}
			if(++frames%480==0){
				System.gc();
			}
			if(eventHandler.mouse1Clicked) {
				eventHandler.mouse1Clicked=false;
			}
			if(eventHandler.mouse2Clicked) {
				eventHandler.mouse2Clicked=false;
			}
			try {
				finishedTime=System.nanoTime();
				remainingTime=(nextDrawTime-System.nanoTime())/1000000;
				if(remainingTime<0) {
					remainingTime=0;
				}
				Thread.sleep((long) remainingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error: Failed to sleep thread", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	};
	public static void addMenuItem(JMenu menu, String name, int hk) {
		JMenuItem menuItem=new JMenuItem(name, hk);
		menu.add(menuItem);
		menuItem.addActionListener(gamePanel);
	}
	public static void addButton(String command, Image icon, JPanel panel) {
		JButton button = new JButton(new ImageIcon(icon));
		button.setPreferredSize(new Dimension(32, 32));
		button.setActionCommand(command);
		button.addActionListener(gamePanel);
		button.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
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
			
		});
		panel.add(button);
	}
	public static void addButton(String command, Image icon, String tooltip, JPanel panel) {
		JButton button = new JButton(new ImageIcon(icon));
		button.setPreferredSize(new Dimension(32, 32));
		button.setActionCommand(command);
		button.setToolTipText(tooltip);
		button.addActionListener(gamePanel);
		button.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentResized(ComponentEvent e) {
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
	
	public static void start(String[] args) {
		JMenu menuFile, menuEntity, menuWorld, menuTile, menuPlayer;
		gamePanel = new LevelEditor();
		gamePanel.frame = new JFrame("Level Editor");
		gamePanel.setFocusable(true);
		gamePanel.frame.getContentPane().add(gamePanel,BorderLayout.CENTER);
		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		menuEntity = new JMenu("Entity");
		menuEntity.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuEntity);
		menuWorld = new JMenu("World");
		menuWorld.setMnemonic(KeyEvent.VK_W);
		menuBar.add(menuWorld);
		menuPlayer = new JMenu("Player");
		menuPlayer.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menuPlayer);
		menuTile = new JMenu("Tile");
		menuTile.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menuTile);
		
		addMenuItem(menuFile, "New", KeyEvent.VK_N);
		
		addMenuItem(menuFile, "Save", KeyEvent.VK_S);
		
		addMenuItem(menuFile, "Load", KeyEvent.VK_L);
		
		addMenuItem(menuEntity, "Add Entity", KeyEvent.VK_A);
		
		addMenuItem(menuEntity, "Remove Entity", KeyEvent.VK_R);
		
		addMenuItem(menuEntity, "Edit Entity", KeyEvent.VK_E);
		
		addMenuItem(menuWorld, "Change Sky Color", KeyEvent.VK_C);

		addMenuItem(menuWorld, "Test", KeyEvent.VK_T);

		addMenuItem(menuWorld, "Zoom In", KeyEvent.VK_I);

		addMenuItem(menuWorld, "Zoom Out", KeyEvent.VK_O);

		addMenuItem(menuWorld, "Change Gravity", KeyEvent.VK_G);

		addMenuItem(menuPlayer, "Change Player Start", KeyEvent.VK_S);

		addMenuItem(menuPlayer, "Change Player Health", KeyEvent.VK_H);

		addMenuItem(menuPlayer, "Change Player Mana", KeyEvent.VK_M);

		addMenuItem(menuPlayer, "Change Player Weapon", KeyEvent.VK_W);

		addMenuItem(menuTile, "Clear Custom Tiles", KeyEvent.VK_C);
		
		gamePanel.frame.setJMenuBar(menuBar);
		gamePanel.frame.pack();
		
		menu = new JFrame("Level Editor UI");
		menu.setFocusable(false);
		
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JTabbedPane tabbedPane2 = new JTabbedPane();
		JPanel p1 = new JPanel(), p2 = new JPanel(), p3 = new JPanel(), p4 = new JPanel(), p5 = new JPanel(), p6 = new JPanel(), p7 = new JPanel();
		customTilePanel=new JPanel();
		tabbedPane.add("Tile", tabbedPane2);
		tabbedPane.add("Entity", p1);
		tabbedPane.add("Tools", p4);
		tabbedPane2.add("Default", p5);
		tabbedPane2.add("Custom", p6);
		menu.add(tabbedPane);
		menu.pack();
		BufferedImage iconAddDecoration;

		p2.setPreferredSize(new Dimension(180, 115));
		p2.setBorder(BorderFactory.createTitledBorder("Living"));

		p3.setPreferredSize(new Dimension(180, 115));
		p3.setBorder(BorderFactory.createTitledBorder("Map"));

		p7.setPreferredSize(new Dimension(180, 200));
		p7.setBorder(BorderFactory.createTitledBorder("Entity Settings"));
		
		p1.add(p2);
		p1.add(p3);
		p1.add(p7);
		
		try {
			iconFireEnemy = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/enemy/redfiresprite.png"));
			iconFireEnemyBlue = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/enemy/bluefiresprite.png"));
			iconThing = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/ThingSingular.png"));
			iconManaOrb = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/manaorb.png"));
			iconPlatform = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/platform.png"));
			iconFlameDemon = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/FlameDemonSingular.png"));
			iconPuddleMonster = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/PuddleMonsterSingular.png"));
			iconZombieKnight = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/ZombieKnightSingular.png"));
			fillTool = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/FillTool.png"));
			iconAddDecoration = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/AddDecoration.png"));
		} catch (IOException e) {
			iconFireEnemy = iconFireEnemyBlue = iconThing = iconManaOrb = iconPlatform = iconFlameDemon = iconPuddleMonster = null;
			iconAddDecoration = null;
			e.printStackTrace();
		}
		addButton("SelectEntity 0", iconFireEnemy, "Fire Enemy", p2);

		addButton("SelectEntity 1", iconFireEnemyBlue, "Blue Fire Enemy", p2);

		addButton("SelectEntity 2", iconThing, "Thing", p2);

		addButton("SelectEntity 5", iconFlameDemon, "Flame Demon", p2);

		addButton("SelectEntity 6", iconPuddleMonster, "Puddle Monster", p2);

		addButton("SelectEntity 7", iconZombieKnight, "Zombie Knight", p2);
		
		addButton("SelectEntity 3", iconManaOrb, "Mana Orb", p3);

		addButton("SelectEntity 4", iconPlatform, "Floating Platform", p3);

		addButton("AddDecoration", iconAddDecoration, "Add Decoration", p3);
		
		BufferedImage iconDraw, iconNew, iconMove, iconDelete, iconEdit, iconFill;
		try {
			iconDraw = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/DrawTiles.png"));
			iconNew = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/AddEntity.png"));
			iconMove = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/MoveEntity.png"));
			iconDelete = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/RemoveEntity.png"));
			iconEdit = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/EditEntity.png"));
			iconFill = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/FillTiles.png"));
		} catch (IOException e) {
			iconDraw = iconNew = iconMove = iconDelete = iconEdit = iconFill = null;
			e.printStackTrace();
		}
		addButton("Tool 0", iconDraw, "Draw Tiles", p4);
		
		addButton("Tool 4", iconFill, "Fill Tiles", p4);
		
		addButton("Tool 1", iconNew, "Add Entity", p4);

		addButton("Tool 2", iconMove, "Move Entity", p4);

		addButton("Tool 3", iconDelete, "Remove Entity", p4);
		
		BufferedImage tilemap;
		try {
			tilemap = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/tilemap.png"));
		} catch (IOException e) {
			tilemap = null;
			e.printStackTrace();
		}
		p5.setLayout(new GridLayout(0, 3));
		int texId = 0;
		for (int i=0; i<Tile.maxTile+1; i++) {
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			texId = Tile.tiles[i].getTextureId();
			int frameX = (texId % 16) * 16;
			int frameY = (texId / 16) * 16;
			g2.drawImage(tilemap, 0, 0, 16, 16,frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
			addButton("tile "+String.valueOf(i), img, p5);
		}
		p6.setLayout(new BoxLayout(p6, BoxLayout.Y_AXIS));
		//customTilePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		customTilePanel.setBorder(BorderFactory.createTitledBorder("Created Tiles"));
		customTilePanel.setPreferredSize(new Dimension(190, 100));
		customTilePanel.setVisible(true);
		p6.add(customTilePanel);
		JPanel addTile = new JPanel();
		//addTile.setLayout(new BoxLayout(addTile, BoxLayout.Y_AXIS));
		addButton("addtile choosefile", "Choose Tile Image", addTile);
		JCheckBox b1 = new JCheckBox("Tile collision", true);
		JCheckBox b2 = new JCheckBox("Does damage");
		JCheckBox b3 = new JCheckBox("Auto detect custom hitbox");
		addTile.add(b1);
		addTile.add(b2);
		addTile.add(b3);
		cbl = new CheckBoxListener(new JCheckBox[] {b1, b2, b3});
		addTile.add(new JLabel("Name (optional)"));
		addTile.add(nameField = new JTextField(12));
		
		addButton("addtile submit", "Create Tile", addTile);
		p6.add(addTile);
		menu.setSize(200,700);
		menu.setVisible(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.frame.setIconImage(iconEdit);
		menu.setIconImage(iconEdit);
		
		gamePanel.startGameThread();
	}
}
