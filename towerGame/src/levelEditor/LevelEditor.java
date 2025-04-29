package levelEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.Bomb;
import entity.Decoration;
import entity.Entity;
import entity.FireEnemy;
import entity.FlameDemon;
import entity.FloatingPlatform;
import entity.ManaOrb;
import entity.PuddleMonster;
import entity.Thing;
import entity.ZombieKnight;
import gui.GUI;
import main.Main;
import main.Renderer;
import map.CustomTile;
import map.Level;
import map.Tile;
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
	static BufferedImage iconFireEnemy, iconFireEnemyBlue, iconThing, iconManaOrb, iconPlatform, iconFlameDemon, iconPuddleMonster, iconZombieKnight, iconBomb, fillTool;
	static CustomTile createdTile;
	static CheckBoxListener cbl;
	static Entity selectedEntity;
	static Decoration placeableDecoration;
	static TileData placeTileData;
	static String[] commands = new String[9];
	public static TilePanel tilePanel;
	private static Map<String,Consumer<String[]>> actions = new HashMap<String,Consumer<String[]>>(); 
	public static double playerHealth = 10.0;
	public static double playerMana = 15.0;
	public static int playerWeapon = Weapon.staff.id;
	public static double playerSpeed = 1.0;
	
	public static HashMap<String,BufferedImage> customSprites = new HashMap<String,BufferedImage>();
	
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
	public static void addAction(String name, Consumer<String[]> action) {
		actions.put(name, action);
	}
	public static Consumer<String[]> getAction(String name) {
		return actions.get(name);
	}
	public static Consumer<String[]> removeAction(String name) {
		return actions.remove(name);
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
			int[] positions = LevelEditorUtils.getTilePosFromMouse();
			double[] positions2 = LevelEditorUtils.getUnroundedTilePosFromMouse();
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
				case 8:
					entitysprite=iconBomb;
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
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		if(eventHandler.debugPressed) {
			g2.setColor(new Color(128, 0, 0, 192));
			GUI.fontRenderer.drawText(g2, "Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M", 10, 10);
		}
		g2.setColor(new Color(0, 0, 0, 192));
		int[] positions = LevelEditorUtils.getTilePosFromMouse();
		
		g2.setXORMode(Color.BLACK);
		GUI.fontRenderer.drawText(g2, "X " + String.valueOf(positions[0]), 10, (Main.scale*240) - 40);
		GUI.fontRenderer.drawText(g2, "Y " + String.valueOf(positions[1]), 10, (Main.scale*240) - 20);
		g2.setPaintMode();
		
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
					playerHealth = 10.0;
					playerMana = 15.0;
					playerWeapon = Weapon.staff.id;
				}
			}
			if(ac=="Resize Level") {
				String userInput = JOptionPane.showInputDialog(null, "Level size X", "Resize Level", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					int levelSizeX=Integer.parseInt(userInput);
					userInput = JOptionPane.showInputDialog(null, "Level size Y", "Resize Level", JOptionPane.QUESTION_MESSAGE);
					int levelSizeY=Integer.parseInt(userInput);
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
					
				}
			}
			
			if(ac=="Change Sky Color") {
				level.skyColor = JColorChooser.showDialog(this, "Choose Color", new Color(98,204,249));
			}
			if(ac=="Change Start") {
				String userInput = JOptionPane.showInputDialog(null, "Player start X", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					level.playerStartX=Double.parseDouble(userInput);
					userInput = JOptionPane.showInputDialog(null, "Player start Y", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
					level.playerStartY=Double.parseDouble(userInput);
				}
			}
			if(ac=="Change Health") {
				String userInput = JOptionPane.showInputDialog(null, "Player health:", "Change Player Health", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					playerHealth = Double.parseDouble(userInput);
				}
			}
			if(ac=="Change Mana") {
				String userInput = JOptionPane.showInputDialog(null, "Player mana:", "Change Player Mana", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					playerMana = Double.parseDouble(userInput);
				}
			}
			if(ac=="Change Weapon") {
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
			if(ac == "Change Speed") {
				String userInput = JOptionPane.showInputDialog(null, "Player speed:", "Change Player Speed", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					playerSpeed = Double.parseDouble(userInput);
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
							} catch(InterruptedException e) {
								return;
							}
						}
						Main.changeZoom(oldZoom);
					}
				}.start();
			}
			if(ac=="Zoom In") {
				LevelEditorUtils.zoomIn();
			}
			if(ac=="Zoom Out") {
				LevelEditorUtils.zoomOut();
			}
			
			if((ac.split(" ")[0]).equals("Tool")) {
				gamePanel.tool = Tool.fromNumber(Integer.valueOf(ac.split(" ")[1]));
			}
			/*if(ac=="Clear Custom Tiles") {
				clearCustomTiles();
			}*/

			if(ac=="Change Gravity (may not work properly)") {
				String userInput = JOptionPane.showInputDialog(null, "Gravity:", "Change Gravity", JOptionPane.QUESTION_MESSAGE);
				if(userInput!=null) {
					level.gravity = (Double.parseDouble(userInput) / 60);
					if(level.gravity == 0)
						level.gravity = 0.000000000000001;
				}
			}
			String[] split = ac.split(";");
			//if(split == null)
			//	split = new String[] {ac};
			Consumer<String[]> action = actions.get(split[0]);
			if (action != null) {
				action.accept(split);
				System.out.println("hit: " + split[0]);
			} else {
				System.out.println("miss: " + split[0]);
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
					case 8:
						e = new Bomb(level);
						break;
					default:
						e = null;
					}
					if(eventHandler.shiftPressed) {
						double[] positions = LevelEditorUtils.getUnroundedTilePosFromMouse();
						e.setPosition(positions[0]-0.5,positions[1]-0.5);
					}else {
						int[] positions = LevelEditorUtils.getTilePosFromMouse();
						e.setPosition(positions[0],positions[1]);
					}
					level.addEntity(e);
					break;
				case MOVEENTITY:
					mp = new Rectangle(7,7,2,2);
					double[] positions1 = LevelEditorUtils.getUnroundedTilePosFromMouse();
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
					double[] positions = LevelEditorUtils.getUnroundedTilePosFromMouse();
				
					for (int i = level.entities.size(); i-- > 0;) { // Remove the one on top
						Entity e2 = level.entities.get(i);
						if(CollisionChecker.checkHitboxes(mp, e2.hitbox, positions[0], positions[1], e2.x, e2.y)) {
							e2.markedForRemoval = true;
							break;
						}
					}
					break;
				case FILLTILES:
					int[] positions2 = LevelEditorUtils.getTilePosFromMouse();
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
						double[] positions3 = LevelEditorUtils.getUnroundedTilePosFromMouse();
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
					int[] positions = LevelEditorUtils.getTilePosFromMouse();
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
					int[] positions = LevelEditorUtils.getTilePosFromMouse();
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
	
	public static void start(String[] args) {
		JMenu menuFile, menuEntity, menuWorld, menuTile, menuPlayer, menuView;
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
		menuView = new JMenu("View");
		menuView.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menuView);
		
		LevelEditorUtils.addMenuItem(menuFile, "New", KeyEvent.VK_N);
		
		LevelEditorUtils.addMenuItem(menuFile, "Save", KeyEvent.VK_S);
		
		LevelEditorUtils.addMenuItem(menuFile, "Load", KeyEvent.VK_L);
		
		LevelEditorUtils.addMenuItem(menuEntity, "Add Entity", KeyEvent.VK_A);
		
		LevelEditorUtils.addMenuItem(menuEntity, "Remove Entity", KeyEvent.VK_X);
		
		LevelEditorUtils.addMenuItem(menuEntity, "Edit Entity", KeyEvent.VK_E);
		
		LevelEditorUtils.addMenuItem(menuWorld, "Resize Level", KeyEvent.VK_R);
		
		LevelEditorUtils.addMenuItem(menuWorld, "Change Sky Color", KeyEvent.VK_C);

		LevelEditorUtils.addMenuItem(menuWorld, "Test", KeyEvent.VK_T);

		LevelEditorUtils.addMenuItem(menuWorld, "Zoom In", KeyEvent.VK_I);

		LevelEditorUtils.addMenuItem(menuWorld, "Zoom Out", KeyEvent.VK_O);

		LevelEditorUtils.addMenuItem(menuWorld, "Change Gravity (may not work properly)", KeyEvent.VK_G);

		LevelEditorUtils.addMenuItem(menuPlayer, "Change Start", KeyEvent.VK_S);

		LevelEditorUtils.addMenuItem(menuPlayer, "Change Health", KeyEvent.VK_H);

		LevelEditorUtils.addMenuItem(menuPlayer, "Change Mana", KeyEvent.VK_M);

		LevelEditorUtils.addMenuItem(menuPlayer, "Change Weapon", KeyEvent.VK_W);

		LevelEditorUtils.addMenuItem(menuPlayer, "Change Speed", KeyEvent.VK_P);

		LevelEditorUtils.addMenuItem(menuTile, "Clear Custom Tiles", KeyEvent.VK_C);
		
		LevelEditorUtils.addCheckBoxMenuItem(menuView, "Tile", "ToggleView;Tile");
		
		gamePanel.frame.setJMenuBar(menuBar);
		gamePanel.frame.pack();

		try {
			iconFireEnemy = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/enemy/redfiresprite.png"));
			iconFireEnemyBlue = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/enemy/bluefiresprite.png"));
			iconThing = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/ThingSingular.png"));
			iconManaOrb = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/manaorb.png"));
			iconPlatform = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/platform.png"));
			iconFlameDemon = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/FlameDemonSingular.png"));
			iconPuddleMonster = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/PuddleMonsterSingular.png"));
			iconZombieKnight = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/ZombieKnightSingular.png"));
			iconBomb = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/bomb.png"));
			fillTool = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/FillTool.png"));
		} catch (IOException e) {
			iconFireEnemy = iconFireEnemyBlue = iconThing = iconManaOrb = iconPlatform = iconFlameDemon = iconPuddleMonster = null;
			e.printStackTrace();
		}
		
		menu = new JFrame("Level Editor UI");
		menu.setFocusable(false);
		
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tilePanel = new TilePanel(gamePanel);
		EntityPanel entityPanel = new EntityPanel(gamePanel);
		JPanel p4 = new JPanel();
		
		tabbedPane.add("Tile", tilePanel);
		tabbedPane.add("Entity", entityPanel);
		tabbedPane.add("Tools", p4);
		menu.add(tabbedPane);
		menu.pack();
		
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
		LevelEditorUtils.addButton("Tool 0", iconDraw, true, "Draw Tiles", p4);
		
		LevelEditorUtils.addButton("Tool 4", iconFill, true, "Fill Tiles", p4);
		
		LevelEditorUtils.addButton("Tool 1", iconNew, true, "Add Entity", p4);

		LevelEditorUtils.addButton("Tool 2", iconMove, true, "Move Entity", p4);

		LevelEditorUtils.addButton("Tool 3", iconDelete, true, "Remove Entity", p4);
		
		
		
		menu.setSize(220,800);
		menu.setVisible(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.frame.setIconImage(iconEdit);
		menu.setIconImage(iconEdit);
		
		addAction("Clear Custom Tiles", (actionArgs) -> LevelEditorUtils.clearCustomTiles());
		
		gamePanel.startGameThread();
	}
}
