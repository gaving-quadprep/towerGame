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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.Decoration;
import entity.Entity;
import entity.FireEnemy;
import entity.FlameDemon;
import entity.FloatingPlatform;
import entity.ManaOrb;
import entity.Thing;
import main.CollisionChecker;
import main.Main;
import map.CustomTile;
import map.Level;
import map.Tile;
import save.SaveFile;
import towerGame.EventHandler;
import towerGame.Player;
@SuppressWarnings("serial")
public class LevelEditor extends JPanel implements Runnable, ActionListener {
	Thread gameThread;
	public JFrame frame;
	public static JFrame menu;
	public static LevelEditor gamePanel;
	LEEventHandler eventHandler = new LEEventHandler(frame);
	EventHandler eventHandler2 = new EventHandler(frame);
	public int drawId = 0;
	public int drawEntity;
	protected boolean debug=false;
	double currentTime, remainingTime, finishedTime;
	Level level = new Level(20, 15, true);
    static boolean testing;
    static JMenuBar menuBar;
    static BufferedImage iconFireEnemy, iconFireEnemyBlue, iconThing, iconManaOrb, iconPlatform, iconFlameDemon, addTileImage;
    static CustomTile createdTile;
    static CheckBoxListener cbl;
    static JPanel customTilePanel;
    static Entity selectedEntity;
    static Decoration placeableDecoration;
	
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
	public int[] getTilePosFromMouse() {
		Point mousePos= MouseInfo.getPointerInfo().getLocation();
		if(mousePos!=null)
			return new int[] {(int)Math.floor((double)(mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX),(int)Math.floor((double)(mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1))};
		return new int[] {-1,-1};
	}
	public double[] getUnroundedTilePosFromMouse() {
		Point mousePos= MouseInfo.getPointerInfo().getLocation();
		if(mousePos!=null)
			return new double[] {(double)(mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX,(double)(mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-1)};
		return new double[] {-1,-1};
	}
	public static void addCustomTileToMenu(CustomTile t) {
		addButton("tile "+t.id, t.texture, customTilePanel);
		menu.invalidate();
		menu.repaint();
	}
	// very original code made by me
	private static Rectangle autoGetHitbox(BufferedImage image) {
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
			g2.setColor(new Color(0,0,0,96));
			int[] positions = getTilePosFromMouse();
			g2.drawRect(positions[0]*Main.tileSize-(int)(level.cameraX*Main.tileSize), positions[1]*Main.tileSize-(int)(level.cameraY*Main.tileSize), Main.tileSize, Main.tileSize);
			
			Point mousePos = MouseInfo.getPointerInfo().getLocation();
			if(mousePos!=null) {
				switch(drawId) {
				case 0:
				case 4:
					if(eventHandler.tileBrush < 256) {
						int frameX = (Tile.tiles[eventHandler.tileBrush].getTextureId() % 16) * 16;
						int frameY = (Tile.tiles[eventHandler.tileBrush].getTextureId() / 16) * 16;
						g2.drawImage(level.tilemap, mousePos.x-LevelEditor.gamePanel.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), mousePos.x-LevelEditor.gamePanel.frame.getLocation().x+(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*0.5), frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
					}else {
						g2.drawImage(((CustomTile)Tile.tiles[eventHandler.tileBrush]).texture, mousePos.x-LevelEditor.gamePanel.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), Main.tileSize, Main.tileSize, (ImageObserver) null);
					}
					break;
				case 1:
					BufferedImage entitysprite;
					int sizeX = 16, sizeY = 16;
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
						sizeX=32;
						sizeY=32;
						break;
					default:
						entitysprite=null;
					}
					g2.drawImage(entitysprite, mousePos.x-LevelEditor.gamePanel.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), sizeX*Main.scale, sizeY*Main.scale, (ImageObserver) null);

				case 5:
					if(placeableDecoration != null) {
						g2.drawImage(placeableDecoration.sprite, mousePos.x-LevelEditor.gamePanel.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), placeableDecoration.imageSizeX*Main.scale, placeableDecoration.imageSizeY*Main.scale, (ImageObserver) null);
					}
				}
			}
		}catch(Exception e) {
    		e.printStackTrace();
    		JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		if(eventHandler.debugPressed) {
			g2.setColor(new Color(128,0,0,192));
			g2.drawString("TowerGame Level Editor version "+Main.version,10,20);
			g2.drawString(String.valueOf(level.entities.size()) + " entities",10,30);
			g2.drawString("Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,40);
		}
		if(eventHandler.mouseCoordsTool) {
			g2.setColor(new Color(0,0,0,192));
			int[] positions = getTilePosFromMouse();
			g2.drawString("X "+String.valueOf(positions[0]),10,(Main.scale*240)-20);
			g2.drawString("Y "+String.valueOf(positions[1]),10,(Main.scale*240)-10);
		}
		
		g2.dispose();
	};
	public void actionPerformed(ActionEvent event) {
		try {
			JFileChooser fc = new JFileChooser();
			String ac = event.getActionCommand();
			if(ac=="Save") {
				fc.setFileFilter(new FileNameExtensionFilter(
				        "TowerGame Level", "tgl"));
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
				        "TowerGame Level", "tgl"));
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveFile.load(level, fc.getSelectedFile().getPath());
				}
			}
			if(ac=="Add Entity") {
				String userInput = JOptionPane.showInputDialog(null, "Entity type", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    if(userInput!=null) {
			    	Entity entity = Entity.entityRegistry.createByName(userInput, new Class[] {Level.class}, new Object[] {level});
			    	if(entity!=null) {
			    		userInput = JOptionPane.showInputDialog(null, "Entity posX", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    		int x=Integer.parseInt(userInput);
			    		userInput = JOptionPane.showInputDialog(null, "Entity posY", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    		int y=Integer.parseInt(userInput);
			    		entity.setPosition(x,y);
			    		level.addEntity(entity);
			    		
			    	}else {
			    		JOptionPane.showMessageDialog(null, "Invalid entity type", "Error", JOptionPane.ERROR_MESSAGE);
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
	    		String userInput = JOptionPane.showInputDialog(null, "Level playerStartX", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
	    		if(userInput!=null) {
	    			level.playerStartX=Integer.parseInt(userInput);
	    			userInput = JOptionPane.showInputDialog(null, "Level playerStartY", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
		    		level.playerStartY=Integer.parseInt(userInput);
	    		}
	    		
			}
			if(ac=="Test") {
	    		level.setPlayer(new Player(level));
	    		level.inLevelEditor=false;
			}
			if((ac.split(" ")[0]).equals("tile")) {
				eventHandler.tileBrush = Integer.valueOf(ac.split(" ")[1]);
				if(gamePanel.drawId != 4)gamePanel.drawId = 0;
			}
			if((ac.split(" ")[0]).equals("SelectEntity")) {
				gamePanel.drawEntity = Integer.valueOf(ac.split(" ")[1]);
				gamePanel.drawId = 1;
			}
			if((ac.split(" ")[0]).equals("Tool")) {
				gamePanel.drawId = Integer.valueOf(ac.split(" ")[1]);
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
					LevelEditor.addCustomTileToMenu(createdTile);
				}else {
					JOptionPane.showMessageDialog(null, "You need to upload a tile image", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			if(ac=="Clear Custom Tiles") {
				for(int i=0; i<256;i++) {
					Tile.tiles[i+256] = null;
					Tile.customTiles[i] = null;
				}
				for(int y=0;y<level.sizeY;y++) {
					for(int x=0;x<level.sizeX;x++) {
						if(level.getTileBackground(x,y) > 255)
							level.setTileBackground(x,y,0);
						if(level.getTileForeground(x,y) > 255)
							level.setTileForeground(x,y,0);
					}
				}
				if(eventHandler.tileBrush > 255)
					eventHandler.tileBrush = 0;
				customTilePanel.removeAll();
				menu.invalidate();
				menu.repaint();
			}

			if(ac=="AddDecoration") {
				fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					BufferedImage decorationImage = ImageIO.read(new File(fc.getSelectedFile().getPath()));
					placeableDecoration = new Decoration(level, decorationImage);
					drawId = 5;
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
	public void floodFill(int x, int y, int tile, int setTile, boolean foreground) {
		if( !(x < 0 || x >= level.sizeX || y < 0 || y >= level.sizeY)) {
			if(tile==setTile) return;
			if(foreground) {
				int t = level.getTileForeground(x,y);
				if(t==tile) {
					level.setTileForeground(x,y,setTile);
					if(level.getTileForeground(x-1,y) == tile)
						floodFill(x-1,y,tile,setTile,foreground);
					if(level.getTileForeground(x+1,y) == tile)
						floodFill(x+1,y,tile,setTile,foreground);
					if(level.getTileForeground(x,y-1) == tile)
						floodFill(x,y-1,tile,setTile,foreground);
					if(level.getTileForeground(x,y+1) == tile)
						floodFill(x,y+1,tile,setTile,foreground);
				}
			}else {
				int t = level.getTileBackground(x,y);
				if(t==tile) {
					level.setTileBackground(x,y,setTile);
					if(level.getTileBackground(x-1,y) == tile)
						floodFill(x-1,y,tile,setTile,foreground);
					if(level.getTileBackground(x+1,y) == tile)
						floodFill(x+1,y,tile,setTile,foreground);
					if(level.getTileBackground(x,y-1) == tile)
						floodFill(x,y-1,tile,setTile,foreground);
					if(level.getTileBackground(x,y+1) == tile)
						floodFill(x,y+1,tile,setTile,foreground);
				}
			}
		}
	}
	
	public void run() {
		double drawInterval=1000000000/60;
		int frames=0;
    	
		while (gameThread!=null) {
			currentTime=System.nanoTime();
			double nextDrawTime=System.nanoTime()+drawInterval;
			//System.out.println("It's running")
			Point mousePos= MouseInfo.getPointerInfo().getLocation();
			if(eventHandler.mouse1Clicked && mousePos!=null) {
				int mx, my;
				Rectangle mp;
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
					case 4:
						e = new FloatingPlatform(level);
						break;
					case 5:
						e = new FlameDemon(level);
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
				case 2:
					mp = new Rectangle(7,7,2,2);
					mx=(int)((mousePos.x-LevelEditor.gamePanel.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5);
					my=(int)((mousePos.y-LevelEditor.gamePanel.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5));
				
					for (int i = level.entities.size(); i-- > 0;) { // Remove the one on top
						Entity e2 = level.entities.get(i);
						if(CollisionChecker.checkHitboxes(mp, e2.hitbox, (double)mx, (double)my, e2.x, e2.y)) {
							// TODO: move entity somehow
							break;
						}
					}
					break;
				case 3:
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
				case 4:
					int[] positions2 = getTilePosFromMouse();
					mx = positions2[0];
					my = positions2[1];
					if(eventHandler.editBackground) {
						floodFill(mx,my,level.getTileBackground(mx, my),eventHandler.tileBrush,false);
					}else {
						floodFill(mx,my,level.getTileForeground(mx, my),eventHandler.tileBrush,true);
						
					}
					break;
				case 5:
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
			if(eventHandler.mouse1Pressed && mousePos!=null) {
				switch(drawId) {
				case 0:
					int[] positions = getTilePosFromMouse();
					if(eventHandler.editBackground) {
						level.setTileBackground(positions[0], positions[1], eventHandler.tileBrush);
					}else {
						level.setTileForeground(positions[0], positions[1], eventHandler.tileBrush);
					}
					break;
				}
				
			}
			if(eventHandler.mouse2Pressed) {
				switch(drawId) {
				case 0:
					int[] positions = getTilePosFromMouse();
					if(eventHandler.editBackground) {
						eventHandler.tileBrush=level.getTileBackground(positions[0], positions[1]);
					}else {
						if((eventHandler.tileBrush=level.getTileForeground(positions[0], positions[1]))==0) {
							eventHandler.tileBrush=level.getTileBackground(positions[0], positions[1]);
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
				try {
					level.update(eventHandler2);
				} catch (Exception e) {
					
				}
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
	public static void addButton(String command, Image icon, String tooltip, JPanel panel) {
		JButton button = new JButton(new ImageIcon(icon));
		button.setActionCommand(command);
		button.setToolTipText(tooltip);
		button.addActionListener(gamePanel);
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
	
	public static void main(String[] args) {
	    JMenu menuFile, menuEntity, menuWorld, menuTile;
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
		menuTile = new JMenu("Tile");
		menuTile.setMnemonic(KeyEvent.VK_T);
		menuBar.add(menuTile);
		
		addMenuItem(menuFile, "Save", KeyEvent.VK_S);
		
		addMenuItem(menuFile, "Load", KeyEvent.VK_L);
		
		addMenuItem(menuEntity, "Add Entity", KeyEvent.VK_A);
		
		addMenuItem(menuEntity, "Remove Entity", KeyEvent.VK_R);
		
		addMenuItem(menuEntity, "Edit Entity", KeyEvent.VK_E);
		
		addMenuItem(menuWorld, "New", KeyEvent.VK_N);
		
		addMenuItem(menuWorld, "New Empty", KeyEvent.VK_E);
		
        addMenuItem(menuWorld, "Change Sky Color", KeyEvent.VK_C);

		addMenuItem(menuWorld, "Change Player Start", KeyEvent.VK_P);

		addMenuItem(menuWorld, "Test", KeyEvent.VK_T);

		addMenuItem(menuTile, "Clear Custom Tiles", KeyEvent.VK_C);
		
		gamePanel.frame.setJMenuBar(menuBar);
		gamePanel.frame.pack();
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menu = new JFrame("UI Test");
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JTabbedPane tabbedPane2 = new JTabbedPane();
		JTabbedPane tabbedPane3 = new JTabbedPane();
	    JPanel p2=new JPanel();
	    JPanel p3=new JPanel();
	    JPanel p4=new JPanel(); 
	    JPanel p5=new JPanel(); 
	    JPanel p6=new JPanel();
	    customTilePanel=new JPanel();
		tabbedPane.add("Tile", tabbedPane2);
		tabbedPane.add("Entity", tabbedPane3);
		tabbedPane.add("Tools", p4);
		tabbedPane2.add("Default", p5);
		tabbedPane2.add("Custom", p6);
		tabbedPane3.add("Living", p2);
		tabbedPane3.add("Map", p3);
		menu.add(tabbedPane);
		menu.pack();
		BufferedImage iconAddDecoration;
		try {
			iconFireEnemy = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/redfiresprite.png"));
			iconFireEnemyBlue = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/bluefiresprite.png"));
			iconThing = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/ThingSingular.png"));
			iconManaOrb = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/manaorb.png"));
			iconPlatform = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/platform.png"));
			iconFlameDemon = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/FlameDemonSingular.png"));
			iconAddDecoration = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/AddDecoration.png"));
		} catch (IOException e) {
			iconFireEnemy = iconFireEnemyBlue = iconThing = iconManaOrb = iconPlatform = iconFlameDemon = null;
			iconAddDecoration = null;
			e.printStackTrace();
		}
		addButton("SelectEntity 0", iconFireEnemy, "Fire Enemy", p2);

		addButton("SelectEntity 1", iconFireEnemyBlue, "Blue Fire Enemy", p2);

		addButton("SelectEntity 2", iconThing, "Thing", p2);

		addButton("SelectEntity 5", iconFlameDemon, "Fire Demon", p2);

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
		customTilePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		customTilePanel.setSize(200, 50);
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
		cbl = new CheckBoxListener(new JCheckBox[] {b1, b2, b3}) ;
		addButton("addtile submit", "Create Tile", addTile);
		p6.add(addTile);
		menu.setSize(200,600);
		menu.setVisible(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.frame.setIconImage(iconEdit);
		menu.setIconImage(iconEdit);
		
    	gamePanel.startGameThread();
	}
}
