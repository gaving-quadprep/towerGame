package levelEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;

import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.Entity;
import entity.FireEnemy;
import entity.ManaOrb;
import entity.Thing;
import main.Main;
import map.Level;
import map.Tile;
import save.SaveFile;
public class LevelEditor extends JPanel implements Runnable, ActionListener {
	Thread gameThread;
	public static JFrame frame;
	EventHandler eventHandler = new EventHandler(frame);
	protected boolean debug=false;
	double currentTime, remainingTime, finishedTime;
	Level level = new Level(16,16);
    Point mousePos;	
    static JMenuBar menuBar;
	
	public LevelEditor() {
		this.addKeyListener(eventHandler);
		this.addMouseListener(eventHandler);
		this.setPreferredSize(new Dimension(320*Main.scale,240*Main.scale));
		this.setDoubleBuffered(true);
		this.setBackground(Color.black);
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
				g2.drawImage(level.tilemap, mousePos.x-LevelEditor.frame.getLocation().x-(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*1.5), mousePos.x-LevelEditor.frame.getLocation().x+(int)(Main.tileSize*0.5), mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight()-(int)(Main.tileSize*0.5), frameX, frameY, frameX+16, frameY+16, (ImageObserver)null);
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
			g2.drawString("X "+String.valueOf((int)((mousePos.x-LevelEditor.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5)),10,(Main.scale*240)-20);
			g2.drawString("Y "+String.valueOf((int)((mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5))),10,(Main.scale*240)-10);
		}
		
		g2.dispose();
	};
	public void actionPerformed(ActionEvent event) {
		try {
			JFileChooser fc = new JFileChooser();
			String fileName;
			if(event.getActionCommand()=="Save") {
				int returnVal = fc.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveFile.save(level, fc.getSelectedFile().getPath());
				}
			}
			if(event.getActionCommand()=="Load") {
				int returnVal = fc.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					SaveFile.load(level, fc.getSelectedFile().getPath());
				}
			}
			if(event.getActionCommand()=="Add Entity") {
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
			    		entity.posX=Integer.parseInt(userInput);
			    		userInput = JOptionPane.showInputDialog(null, "Entity posY", "Add Entity", JOptionPane.QUESTION_MESSAGE);
			    		entity.posY=Integer.parseInt(userInput);
			    		if(entity instanceof FireEnemy) {
			    			((FireEnemy)entity).baseY=Integer.parseInt(userInput);
			    		}
			    		level.addEntity(entity);
			    		
			    	}
			    }
			}
			if(event.getActionCommand()=="Remove Entity") {
				Object[] possibleValues = level.entities.toArray();

				Object en = JOptionPane.showInputDialog(null,
				             "Choose an entity", "Remove Entity",
				             JOptionPane.INFORMATION_MESSAGE, null,
				             possibleValues, possibleValues[0]);
				level.entities.remove(en);
			}
			if(event.getActionCommand()=="New") {
	    		String userInput = JOptionPane.showInputDialog(null, "Level sizeX", "New Level", JOptionPane.QUESTION_MESSAGE);
	    		int levelSizeX=Integer.parseInt(userInput);
	    		userInput = JOptionPane.showInputDialog(null, "Level sizeY", "New Level", JOptionPane.QUESTION_MESSAGE);
	    		int levelSizeY=Integer.parseInt(userInput);
				level.mapTilesBackground = new int[0][0];
				level.mapTilesForeground = new int[0][0];
				level = null;
				System.gc();
				level = new Level(levelSizeX, levelSizeY);
			}
			if(event.getActionCommand()=="Change Sky Color") {
				level.skyColor = JColorChooser.showDialog(this, "Choose Color", new Color(98,204,249));
			}
			if(event.getActionCommand()=="Change Player Start") {
	    		String userInput = JOptionPane.showInputDialog(null, "Level playerSpawnX", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
	    		level.playerStartX=Integer.parseInt(userInput);
	    		userInput = JOptionPane.showInputDialog(null, "Level playerSpawnY", "Change Player Start", JOptionPane.QUESTION_MESSAGE);
	    		level.playerStartY=Integer.parseInt(userInput);
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
    	test2.posY=6;
    	test2.posX=8;
    	level.addEntity(test2);
    	
		while (gameThread!=null) {
			currentTime=System.nanoTime();
			double nextDrawTime=System.nanoTime()+drawInterval;
			//System.out.println("It's running");
			mousePos= MouseInfo.getPointerInfo().getLocation();
			if(eventHandler.mouse1Pressed && mousePos!=null) {
				if(eventHandler.editBackground) {
					level.setTileBackground((int)((mousePos.x-LevelEditor.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5),(int)((mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5)),eventHandler.tileBrush);
				}else {
					level.setTileForeground((int)((mousePos.x-LevelEditor.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5),(int)((mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5)),eventHandler.tileBrush);
				}
				
			}
			if(eventHandler.mouse2Pressed) {
				if(eventHandler.editBackground) {
					eventHandler.tileBrush=level.getTileBackground((int)(mousePos.x-LevelEditor.frame.getLocation().x)/Main.tileSize+(int)(level.cameraX),(int)((mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5)));
				}else {
					if((eventHandler.tileBrush=level.getTileForeground((int)((mousePos.x-LevelEditor.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5),(int)((mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5))))==0) {
						eventHandler.tileBrush=level.getTileBackground((int)((mousePos.x-LevelEditor.frame.getLocation().x)/Main.tileSize+level.cameraX+0.5),(int)((mousePos.y-LevelEditor.frame.getLocation().y-menuBar.getHeight())/Main.tileSize+(level.cameraY-0.5)));
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
			if(++frames%480==0){
				System.gc();
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
	
	public static void main(String[] args) {
	    JMenu menu, menuEntity, menuWorld, submenu;
	    JMenuItem menuItemSave, menuItemLoad, menuItemLoadLegacy, menuItemAddEntity, menuItemRemoveEntity, menuItemChangeSkyColor, menuItemNewWorld, menuItemChangePlayerSpawn;
		frame = new JFrame("Level Editor");
		LevelEditor gamePanel=new LevelEditor();
		gamePanel.setFocusable(true);
		frame.getContentPane().add(gamePanel,BorderLayout.CENTER);
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);
		menuEntity = new JMenu("Entity");
		menuEntity.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuEntity);
		menuWorld = new JMenu("World");
		menuWorld.setMnemonic(KeyEvent.VK_W);
		menuBar.add(menuWorld);
		
		menuItemSave=new JMenuItem("Save", KeyEvent.VK_S);
		menu.add(menuItemSave);
        menuItemSave.addActionListener(gamePanel);
		
		menuItemLoad=new JMenuItem("Load", KeyEvent.VK_L);
		menu.add(menuItemLoad);
        menuItemLoad.addActionListener(gamePanel);
		
		menuItemLoadLegacy=new JMenuItem("Load With Old Format", KeyEvent.VK_E);
		menu.add(menuItemLoadLegacy);
        menuItemLoadLegacy.addActionListener(gamePanel);
		
		menuItemAddEntity=new JMenuItem("Add Entity", KeyEvent.VK_A);
		menuEntity.add(menuItemAddEntity);
        menuItemAddEntity.addActionListener(gamePanel);
		
		menuItemRemoveEntity=new JMenuItem("Remove Entity", KeyEvent.VK_R);
		menuEntity.add(menuItemRemoveEntity);
        menuItemRemoveEntity.addActionListener(gamePanel);
		
        menuItemNewWorld=new JMenuItem("New", KeyEvent.VK_N);
		menuWorld.add(menuItemNewWorld);
		menuItemNewWorld.addActionListener(gamePanel);
		
        menuItemChangeSkyColor=new JMenuItem("Change Sky Color", KeyEvent.VK_C);
		menuWorld.add(menuItemChangeSkyColor);
		menuItemChangeSkyColor.addActionListener(gamePanel);

		menuItemChangePlayerSpawn=new JMenuItem("Change Player Start", KeyEvent.VK_P);
		menuWorld.add(menuItemChangePlayerSpawn);
		menuItemChangePlayerSpawn.addActionListener(gamePanel);
		
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    	gamePanel.startGameThread();
	}
}
