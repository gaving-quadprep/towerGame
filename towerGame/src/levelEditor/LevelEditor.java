package levelEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
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

import entity.Entity;
import entity.FireEnemy;
import entity.ManaOrb;
import entity.Thing;
import main.CollisionChecker;
import main.Main;
import map.Level;
import map.Tile;
import save.SaveFile;
import towerGame.EventHandler;
import towerGame.Player;
@SuppressWarnings("serial")
public class LevelEditor extends JPanel implements Runnable, ActionListener {
	Thread gameThread;
	public JFrame frame;
	public static LevelEditor gamePanel;
	LEEventHandler eventHandler = new LEEventHandler(frame);
	EventHandler eventHandler2 = new EventHandler(frame);
	public int drawId = 0;
	public int drawEntity;
	protected boolean debug=false;
	double currentTime, remainingTime, finishedTime;
	Level level = new Level(16,16,true);
    Point mousePos;
    static boolean testing;
    static JMenuBar menuBar;
    static BufferedImage iconFireEnemy, iconFireEnemyBlue, iconThing, iconManaOrb;
	
	public LevelEditor() {
		this.addKeyListener(eventHandler);
		this.addMouseListener(eventHandler);
		this.addKeyListener(eventHandler2);
		this.addMouseListener(eventHandler2);
		this.setPreferredSize(new Dimension(320*Main.scale,240*Main.scale));
		this.setDoubleBuffered(true);
		this.setBackground(Color.black);
	}
	private void writeObject(ObjectOutputStream oos) throws IOException {
	    throw new NotSerializableException();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(level.skyColor);
		g2.fillRect(0, 0, 320*Main.scale, 240*Main.scale);
		try {
			if(eventHandler.editBackground) {
				level.renderBackgroundOnly(g2);
			}else {
				level.render(g2);
			}
			int frameX = (Tile.tiles[eventHandler.tileBrush].getTextureId() % 16) * 16;
			int frameY = (Tile.tiles[eventHandler.tileBrush].getTextureId() / 16) * 16;
			if(mousePos!=null) {
				switch(drawId) {
				case 0:
					g2.drawImage(level.tilemap, mousePos.x-LevelEditor.gamePanel.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), mousePos.x-LevelEditor.gamePanel.frame.getLocation().x+(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*0.5), frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
					break;
				case 1:
					BufferedImage entitysprite;
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
					default:
						entitysprite=null;
					}
					g2.drawImage(entitysprite, mousePos.x-LevelEditor.gamePanel.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), Main.tileSize, Main.tileSize, (ImageObserver) null);
				}
			}
		}catch(Exception e) {
    		JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		e.printStackTrace();
		}
		if(eventHandler.debugPressed) {
			g2.setColor(new Color(128,0,0,192));
			g2.drawString("TowerGame Level Editor version 0.1",10,20);
			g2.drawString("E "+String.valueOf(level.entities.size()),10,30);
			g2.drawString("M "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,40);
		}
		if(eventHandler.mouseCoordsTool) {
			g2.setColor(new Color(128,0,0,192));
			g2.drawString("X "+String.valueOf((int)((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5)),10,(Main.scale*240)-20);
			g2.drawString("Y "+String.valueOf((int)((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5))),10,(Main.scale*240)-10);
		}
		
		g2.dispose();
	};
	public void actionPerformed(ActionEvent event) {
		try {
			JFileChooser fc = new JFileChooser();
			String ac = event.getActionCommand();
			if(ac=="Save") {
				int returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveFile.save(level, fc.getSelectedFile().getPath());
				}
			}
			if(ac=="Load") {
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveFile.load(level, fc.getSelectedFile().getPath());
				}
			}
			if(ac=="Add Entity") {
				String userInput = JOptionPane.showInputDialog(null, "Entity type (New UI coming soon)", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    if(userInput!=null) {
			    	Entity entity = null;
			    	if(userInput.contains("FireEnemy")) {
			    		userInput = JOptionPane.showInputDialog(null, "Entity isBlue", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    		entity = new FireEnemy(level, Boolean.parseBoolean(userInput));
			    	}
			    	if(userInput.contains("Thing")) {
			    		entity = new Thing(level);
			    	}
			    	if(userInput.contains("ManaOrb")) {
			    		entity = new ManaOrb(level);
			    	}
			    	if(entity!=null) {
			    		userInput = JOptionPane.showInputDialog(null, "Entity posX", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    		entity.x=Integer.parseInt(userInput);
			    		userInput = JOptionPane.showInputDialog(null, "Entity posY", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    		entity.y=Integer.parseInt(userInput);
			    		if(entity instanceof FireEnemy) {
			    			((FireEnemy)entity).baseY=Integer.parseInt(userInput);
			    		}
			    		level.addEntity(entity);
			    		
			    	}
			    }
			}
			if(ac=="Remove Entity") {
				Object[] possibleValues = level.entities.toArray();

				Object en = JOptionPane.showInputDialog(null,
				             "Choose an entity", "Remove Entity",
				             JOptionPane.INFORMATION_MESSAGE, null,
				             possibleValues, possibleValues[0]);
				level.entities.remove(en);
			}
			if(ac=="New") {
	    		String userInput = JOptionPane.showInputDialog(null, "Level sizeX", "New Level", JOptionPane.QUESTION_MESSAGE);
	    		if(userInput!=null) {
		    		int levelSizeX=Integer.parseInt(userInput);
		    		userInput = JOptionPane.showInputDialog(null, "Level sizeY", "New Level", JOptionPane.QUESTION_MESSAGE);
		    		int levelSizeY=Integer.parseInt(userInput);
					level = null;
					System.gc();
					level = new Level(levelSizeX, levelSizeY,true);
	    		}
			}
			if(ac=="New Empty") {
	    		String userInput = JOptionPane.showInputDialog(null, "Level sizeX", "New Level", JOptionPane.QUESTION_MESSAGE);
	    		if(userInput!=null) {
		    		int levelSizeX=Integer.parseInt(userInput);
		    		userInput = JOptionPane.showInputDialog(null, "Level sizeY", "New Level", JOptionPane.QUESTION_MESSAGE);
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
	    		}
			}
			if(ac=="Change Sky Color") {
				level.skyColor = JColorChooser.showDialog(this, "Choose Color", new Color(98,204,249));
			}
			if(ac=="Change Player Start") {
	    		String userInput = JOptionPane.showInputDialog(null, "Level playerSpawnX", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
	    		if(userInput!=null) {
	    			level.playerStartX=Integer.parseInt(userInput);
	    			userInput = JOptionPane.showInputDialog(null, "Level playerSpawnY", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
		    		level.playerStartY=Integer.parseInt(userInput);
	    		}
	    		
			}
			if(ac=="Test (does not work yet)") {
	    		level.setPlayer(new Player(level));
	    		level.inLevelEditor=false;
			}
			if((ac.split(" ")[0]).equals("tile")) {
				eventHandler.tileBrush = Integer.valueOf(ac.split(" ")[1]);
			}
			if((ac.split(" ")[0]).equals("SelectEntity")) {
				gamePanel.drawEntity = Integer.valueOf(ac.split(" ")[1]);
			}
			if((ac.split(" ")[0]).equals("Tool")) {
				gamePanel.drawId = Integer.valueOf(ac.split(" ")[1]);
			}
			if(ac=="addtile choosefile") {
				fc.showOpenDialog(this);
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
		
    	FireEnemy test2=new FireEnemy(level,true);
    	test2.baseY=6;
    	test2.y=6;
    	test2.x=8;
    	level.addEntity(test2);
    	
		while (gameThread!=null) {
			currentTime=System.nanoTime();
			double nextDrawTime=System.nanoTime()+drawInterval;
			//System.out.println("It's running")
			mousePos= MouseInfo.getPointerInfo().getLocation();
			if(eventHandler.mouse1Clicked && mousePos!=null) {
				switch(drawId) {
				case 1:
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
					default:
						e = null;
					}
					e.setPosition((int)((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5),(int)((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5)));
					level.addEntity(e);
					break; //delete this to automatically remove entities on the same tile when you place them (it's not a bug it's a feature)
				case 3:
					Rectangle mp = new Rectangle(7,7,1,1);
					int mx=(int)((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5);
					int my=(int)((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5));
				
					for (int i = level.entities.size(); i-- > 0;) { // Remove the one on top
						Entity e2 = level.entities.get(i);
						if(CollisionChecker.checkHitboxes(mp, e2.hitbox, (double)mx, (double)my, e2.x, e2.y)) {
							e2.markedForRemoval = true;
							break;
						}
					}
					
				}
				
			}
			if(eventHandler.mouse1Pressed && mousePos!=null) {
				switch(drawId) {
				case 0:
					if(eventHandler.editBackground) {
						level.setTileBackground((int)Math.round((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX),(int)Math.round((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1)),eventHandler.tileBrush);
					}else {
						level.setTileForeground((int)Math.round((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX),(int)Math.round((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1)),eventHandler.tileBrush);
					}
					break;
				
				}
				
			}
			if(eventHandler.mouse2Pressed) {
				switch(drawId) {
				case 0:
					if(eventHandler.editBackground) {
						eventHandler.tileBrush=level.getTileBackground((int)Math.round(mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+(int)(level.cameraX),(int)Math.round((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1)));
					}else {
						if((eventHandler.tileBrush=level.getTileForeground((int)Math.round((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX),(int)Math.round((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1))))==0) {
							eventHandler.tileBrush=level.getTileBackground((int)Math.round((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX),(int)Math.round((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1)));
						}
					}
				}
				
				
			}
			if(eventHandler.downPressed) {
				level.cameraY+=0.14;
			}
			if(eventHandler.upPressed) {
				level.cameraY-=0.14;
			}
			if(eventHandler.leftPressed) {
				level.cameraX-=0.14;
			}
			if(eventHandler.rightPressed) {
				level.cameraX+=0.14;
			}
			repaint();
			if(level!=null) {
				level.update(eventHandler2);
			}
			Main.frames++;
			if(++frames%480==0){
				System.gc();
			}
			if(eventHandler.mouse1Clicked) {
				eventHandler.mouse1Clicked=false;
			}
			if(eventHandler.mouse2Clicked) {
				eventHandler.mouse2Clicked=false;
			}
			if(eventHandler2.mouse1Clicked) {
				eventHandler2.mouse1Clicked=false;
			}
			if(eventHandler2.mouse2Clicked) {
				eventHandler2.mouse2Clicked=false;
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
		button.setActionCommand(command);
		button.addActionListener(gamePanel);
		panel.add(button);
	}
	public static void addButton(String command, String text, JPanel panel) {
		JButton button = new JButton(text);
		button.setActionCommand(command);
		button.addActionListener(gamePanel);
		panel.add(button);
	}
	public static void main(String[] args) {
	    JMenu menuFile, menuEntity, menuWorld;
	    gamePanel=new LevelEditor();
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
		
		addMenuItem(menuFile, "Save", KeyEvent.VK_S);
		
		addMenuItem(menuFile, "Load", KeyEvent.VK_L);
		
		addMenuItem(menuFile, "Load With Old Format", KeyEvent.VK_O);
		
		addMenuItem(menuEntity, "Add Entity", KeyEvent.VK_A);
		
		addMenuItem(menuEntity, "Remove Entity", KeyEvent.VK_R);
		
		addMenuItem(menuWorld, "New", KeyEvent.VK_N);
		
		addMenuItem(menuWorld, "New Empty", KeyEvent.VK_E);
		
        addMenuItem(menuWorld, "Change Sky Color", KeyEvent.VK_C);

		addMenuItem(menuWorld, "Change Player Start", KeyEvent.VK_P);

		addMenuItem(menuWorld, "Test (does not work yet)", KeyEvent.VK_T);
		
		gamePanel.frame.setJMenuBar(menuBar);
		gamePanel.frame.pack();
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JFrame frame2 = new JFrame("UI Test");
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JTabbedPane tabbedPane2 = new JTabbedPane();
	    JPanel p2=new JPanel(); 
	    JPanel p3=new JPanel(); 
	    JPanel p4=new JPanel(); 
	    JPanel p5=new JPanel();
		tabbedPane.add("Tile", tabbedPane2);
		tabbedPane.add("Entity", p2);
		tabbedPane.add("Tools", p3);
		tabbedPane2.add("Default", p4);
		tabbedPane2.add("Custom", p5);
		frame2.add(tabbedPane);
		frame2.pack();
		
		try {
			iconFireEnemy = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/redfiresprite.png"));
			iconFireEnemyBlue = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/bluefiresprite.png"));
			iconThing = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/ThingSingular.png"));
			iconManaOrb = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/manaorb.png"));
		} catch (IOException e) {
			iconFireEnemy = iconFireEnemyBlue = iconThing = iconManaOrb = null;
			e.printStackTrace();
		}
		addButton("SelectEntity 0",iconFireEnemy,p2);

		addButton("SelectEntity 1",iconFireEnemyBlue,p2);

		addButton("SelectEntity 2",iconThing,p2);

		addButton("SelectEntity 3",iconManaOrb,p2);
		
		BufferedImage iconDraw, iconNew, iconMove, iconDelete, iconEdit;
		try {
			iconDraw = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/DrawTiles.png"));
			iconNew = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/AddEntity.png"));
			iconMove = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/MoveEntity.png"));
			iconDelete = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/RemoveEntity.png"));
			iconEdit = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/EditEntity.png"));
		} catch (IOException e) {
			iconDraw = iconNew = iconMove = iconDelete = iconEdit = null;
			e.printStackTrace();
		}
		addButton("Tool 0", iconDraw, p3);
		
		addButton("Tool 1", iconNew, p3);

		addButton("Tool 2", iconMove, p3);

		addButton("Tool 3", iconDelete, p3);
		
		BufferedImage tilemap;
		try {
			tilemap = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/tilemap.png"));
		} catch (IOException e) {
			tilemap = null;
			e.printStackTrace();
		}
		p4.setLayout(new GridLayout(13, 3));
		int texId = 0;
		for (int i=0; i<39; i++) {
			BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = img.createGraphics();
			texId = Tile.tiles[i].getTextureId();
			int frameX = (texId % 16) * 16;
			int frameY = (texId / 16) * 16;
			g2.drawImage(tilemap, 0, 0, 16, 16,frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
			addButton("tile "+String.valueOf(i), img, p4);
		}
		p5.setLayout(new BoxLayout(p5, BoxLayout.Y_AXIS));
		p5.add(new JLabel("(Coming Soon)"));
		JPanel addTile = new JPanel();
		//addTile.setLayout(new BoxLayout(addTile, BoxLayout.Y_AXIS));
		addButton("addtile choosefile", "Choose Tile Image", addTile);
		addTile.add(new JCheckBox("Tile collision"));
		addTile.add(new JCheckBox("Does damage"));
		addButton("addtile submit", "Create Tile", addTile);
		p5.add(addTile);
		frame2.setSize(200,500);
		frame2.setVisible(true);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.frame.setIconImage(iconEdit);
		frame2.setIconImage(iconEdit);
		
    	gamePanel.startGameThread();
	}
}
