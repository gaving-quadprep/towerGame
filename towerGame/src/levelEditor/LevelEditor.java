package levelEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import entity.Decoration;
import entity.Entity;
import gui.GUI;
import levelEditor.menu.EntityMenu;
import levelEditor.menu.FileMenu;
import levelEditor.menu.PlayerMenu;
import levelEditor.menu.TileMenu;
import levelEditor.menu.ViewMenu;
import levelEditor.menu.WorldMenu;
import levelEditor.panel.EntityPanel;
import levelEditor.panel.PlayerPanel;
import levelEditor.panel.TilePanel;
import levelEditor.panel.ToolPanel;
import levelEditor.tool.Tool;
import main.Main;
import main.Renderer;
import map.CustomTile;
import map.Level;
import map.interactable.TileData;
import weapon.Weapon;

@SuppressWarnings("serial")
public class LevelEditor extends JPanel implements Runnable, ActionListener {
	Thread gameThread;
	public JFrame frame;
	public static JFrame menu;
	public static LevelEditor gamePanel;
	public LEEventHandler eventHandler = new LEEventHandler(frame);
	public Tool tool = Tool.drawTiles;
	public int drawEntity;
	protected boolean debug=false;
	double currentTime, remainingTime, finishedTime;
	public Level level = new Level(Main.width, Main.height, true);
	{
		Main.worldRenderer.level = level;
	}
	static boolean testing;
	static JMenuBar menuBar;
	public static BufferedImage iconFireEnemy, iconFireEnemyBlue, iconThing, iconManaOrb, iconPlatform, iconFlameDemon, iconPuddleMonster, iconZombieKnight, iconBomb, fillTool;
	CustomTile createdTile;
	CheckBoxListener cbl;
	Entity selectedEntity;
	public static Decoration placeableDecoration;
	public static TileData placeTileData;
	String[] commands = new String[9];
	public static JTabbedPane tabbedPane;
	public static TilePanel tilePanel;
	public static EntityPanel entityPanel;
	public static ToolPanel toolPanel;
	public static PlayerPanel playerPanel;
	public boolean showName = true;
	public boolean showIcon = true;
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

			Main.worldRenderer.drawRect(positions[0], positions[0]+1, positions[1], positions[1]+1,  new Color(0, 0, 0, 96));
			
			// level border
			Main.worldRenderer.drawRect(0, level.sizeX, 0, level.sizeY, Color.WHITE);
			
			tool.render(gamePanel, Main.worldRenderer);
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
		GUI.fontRenderer.drawText(g2, "X " + String.valueOf(positions[0]), Main.scale*3, (Main.scale*240) - Main.scale*15);
		GUI.fontRenderer.drawText(g2, "Y " + String.valueOf(positions[1]), Main.scale*3, (Main.scale*240) - Main.scale*8);
		g2.setPaintMode();
		
		g2.dispose();
	}
	public void actionPerformed(ActionEvent event) {
		try {
			String ac = event.getActionCommand();
			
			String[] split = ac.split(";");
			//if(split == null)
			//	split = new String[] {ac};
			Consumer<String[]> action = actions.get(split[0]);
			if (action != null) {
				action.accept(split);
			} else {
				System.out.println("Command not found: " + split[0]);
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
		
		while (gameThread!=null) {
			currentTime=System.nanoTime();
			double nextDrawTime=System.nanoTime()+drawInterval;
			if(eventHandler.mouse1Clicked)
				tool.onMouseLeftClick(gamePanel);
			if(eventHandler.mouse2Clicked)
				tool.onMouseRightClick(gamePanel);
			if(eventHandler.mouse1Pressed)
				tool.onMouseLeftPressed(gamePanel);
			if(eventHandler.mouse2Pressed)
				tool.onMouseRightPressed(gamePanel);
			tool.update(gamePanel);
			
			repaint();
			
			if(level != null) {
				try {
					level.update();
				} catch (Exception e) {
					
				}
			}
			
			eventHandler.mouse1Clicked = false;
			eventHandler.mouse2Clicked = false;
			
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
		menuFile = new FileMenu(gamePanel, "File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);
		menuEntity = new EntityMenu(gamePanel, "Entity");
		menuEntity.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuEntity);
		menuWorld = new WorldMenu(gamePanel, "World");
		menuWorld.setMnemonic(KeyEvent.VK_W);
		menuBar.add(menuWorld);
		menuPlayer = new PlayerMenu(gamePanel, "Player");
		menuPlayer.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menuPlayer);
		menuTile = new TileMenu(gamePanel, "Tile");
		menuTile.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menuTile);
		menuView = new ViewMenu(gamePanel, "View");
		menuView.setMnemonic(KeyEvent.VK_V);
		menuBar.add(menuView);

		gamePanel.frame.setJMenuBar(menuBar);
		gamePanel.frame.pack();

		iconFireEnemy = LevelEditorUtils.readImage("/sprites/enemy/redfiresprite.png");
		iconFireEnemyBlue = LevelEditorUtils.readImage("/sprites/enemy/bluefiresprite.png");
		iconThing = LevelEditorUtils.readImage("/sprites/levelEditor/ThingSingular.png");
		iconManaOrb = LevelEditorUtils.readImage("/sprites/manaorb.png");
		iconPlatform = LevelEditorUtils.readImage("/sprites/platform.png");
		iconFlameDemon = LevelEditorUtils.readImage("/sprites/levelEditor/FlameDemonSingular.png");
		iconPuddleMonster = LevelEditorUtils.readImage("/sprites/levelEditor/PuddleMonsterSingular.png");
		iconZombieKnight = LevelEditorUtils.readImage("/sprites/levelEditor/ZombieKnightSingular.png");
		iconBomb = LevelEditorUtils.readImage("/sprites/bomb.png");
		fillTool = LevelEditorUtils.readImage("/sprites/levelEditor/FillTool.png");
		
		menu = new JFrame("Level Editor UI");
		menu.setFocusable(false);
		
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tabbedPane = new JTabbedPane();
		tilePanel = new TilePanel(gamePanel);
		entityPanel = new EntityPanel(gamePanel);
		toolPanel = new ToolPanel(gamePanel);
		playerPanel = new PlayerPanel(gamePanel);

		tilePanel.toggle();
		entityPanel.toggle();
		toolPanel.toggle();
		
		menu.add(tabbedPane);
		menu.pack();
		
		BufferedImage editorIcon = LevelEditorUtils.readImage("/sprites/levelEditor/EditEntity.png");
		
		menu.setSize(220,700);
		menu.setVisible(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.frame.setIconImage(editorIcon);
		menu.setIconImage(editorIcon);
		
		gamePanel.startGameThread();
	}
}
