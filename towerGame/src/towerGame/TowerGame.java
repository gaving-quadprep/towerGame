package towerGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.*;
import gui.GUI;
import gui.HealthBarManager;
import gui.PauseMenu;
import levelEditor.LevelEditor;
import main.Main;
import map.Level;
import save.SaveFile;
import util.CollisionChecker;

@SuppressWarnings("serial")
public class TowerGame extends JPanel implements Runnable {
	Thread gameThread;
	public JFrame frame;
	public static TowerGame gamePanel;
	EventHandler eventHandler = new EventHandler(frame);
	public Level level= new Level(16,16);
	HealthBarManager hBarManager = new HealthBarManager();
	String filePath;
	public double remainingTime, drawStart, drawEnd, drawTime;
	public static double playerCheckpointX, playerCheckpointY;
	public static boolean hasWon;
	public ArrayList<GUI> guis = new ArrayList<GUI>();
	public static GUI pauseMenu = new PauseMenu();
	public static boolean isTesting;
	
	public TowerGame() {
		this.addKeyListener(eventHandler);
		this.addMouseListener(eventHandler);
		this.setPreferredSize(new Dimension(320*Main.scale,240*Main.scale));
		this.setDoubleBuffered(true);
		this.setBackground(Color.black);
	}
	private void writeObject(ObjectOutputStream oos) throws IOException {
	    throw new NotSerializableException();
	}
	public void update() {
		try {
			level.update(eventHandler);
		}catch(Exception e) {
    		JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		e.printStackTrace();
		}
	}
	public EventHandler getEventHandler() {
		return this.eventHandler;
	}
	public static void show(GUI gui) {
		if(!gamePanel.guis.contains(gui))
			gamePanel.guis.add(gui);
	}
	public static void hide(GUI gui) {
		gamePanel.guis.remove(gui);
	}
	public static void hideAllOfType(Class<? extends GUI> clazz) {
		gamePanel.guis.removeIf((GUI g) -> g.getClass() == clazz);
	}
	public void paintComponent(Graphics g) {
		drawStart = System.nanoTime();
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setFont(Main.font);
		g2.setColor(level.skyColor);
		g2.fillRect(0, 0, 320*Main.scale, 240*Main.scale);
		try {
			level.render(g2);
			if(level.player!=null) {
				level.entity_lock.lock();
				try {
					for ( GUI gui : guis) {
						if(gui != null) {
							gui.render(g2, level);
						}
					}
				} finally {
					level.entity_lock.unlock();
				}
				
			}
		}catch(Exception e) {
    		JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		e.printStackTrace();
		}
		if(eventHandler.debugPressed) {
			level.entity_lock.lock();
			for(Entity e : level.entities) {
				CollisionChecker.renderDebug(level,e,g2);
			}
			level.entity_lock.unlock();
			CollisionChecker.renderDebug(level,level.player,g2);
			g2.setColor(new Color(128,0,0,192));
			g2.drawString("TowerGame version "+Main.version,10,30);
			g2.drawString("Height "+String.valueOf(level.sizeY-level.player.y),10,40);
			g2.drawString("Frame time "+String.valueOf(drawTime),10,50);
			g2.drawString(String.valueOf(level.entities.size())+ " entities",10,60);
			g2.drawString("Memory: "+String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1000000)+ "M",10,70);
		}
		g2.dispose();
		
		drawEnd = System.nanoTime();
		drawTime = (drawEnd-drawStart)/1000000;
	};
	public void startGameThread() {
		gameThread=new Thread(this);
		gameThread.start();
	};

	@Override
	public void run() {
		double drawInterval=1000000000/Main.fpsCap;
		Player player = new Player(level);
    	level.setPlayer(player);
		show(hBarManager);
    	update();
		try {
			SaveFile.load(level, filePath);
		} catch (Exception e) {
			level = new Level(20, 15);
	    	level.setPlayer(player);
			Entity test = new FireEnemy(level);
	    	test.setPosition(7,6);
	    	level.addEntity(test);
		}
    	playerCheckpointX=level.playerStartX;
    	playerCheckpointY=level.playerStartY;
    	
		while (gameThread!=null) {
			double nextDrawTime=System.nanoTime()+drawInterval;
			if(!eventHandler.paused) {
				update();
				Main.frames++;
			}
			repaint();
			if(level.player.health.compareTo(BigDecimal.ZERO) <= 0) {
				try {
					SaveFile.load(level, filePath);
				} catch (Exception e) {
					gameThread.interrupt();
					System.exit(0);
					return;
				}
		    	hBarManager.refresh();
		    	level.player.yVelocity = 0;
		    	level.player.x = playerCheckpointX;
		    	level.player.y = playerCheckpointY;
		    	level.centerCameraOnPlayer();
		    }
			if(eventHandler.resetPressed) {
				try {
					SaveFile.load(level, filePath);
				} catch (Exception e) {
					level = new Level(20, 15);
			    	level.setPlayer(player);
				}
		    	hBarManager.refresh();
		    	level.player.yVelocity = 0;
		    	playerCheckpointX=level.playerStartX;
		    	playerCheckpointY=level.playerStartY;
		    	level.player.x = playerCheckpointX;
		    	level.player.y = playerCheckpointY;
		    	Main.frames=0;
		    }
			if(hasWon) {
				JOptionPane.showMessageDialog(null, "You win!\nTime: "+String.format("%02.0f", Math.floor((float)Main.frames/3600))+":"+String.format("%05.2f", ((float)Main.frames)/60%60), "Congrats", JOptionPane.INFORMATION_MESSAGE);
				gameThread.interrupt();
				if(!isTesting) {
					System.exit(0);
				}else {
					frame.dispose();
					//gameThread.stop();
				}
				return;
			}
			if((Runtime.getRuntime().freeMemory()) < 100000) {
				System.gc();
			}
			if(eventHandler.mouse1Clicked) {
				eventHandler.mouse1Clicked=false;
			}
			if(eventHandler.mouse2Clicked) {
				eventHandler.mouse2Clicked=false;
			}
			try {
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
		gamePanel=new TowerGame();
		if(args.length > 0) {
			gamePanel.filePath = args[0];
		}

		if(args.length > 1) {
			isTesting=true;
		}else {
			isTesting=false;
		}
		gamePanel.frame = new JFrame("Tower Game");
		gamePanel.frame.setFont(Main.font);
		
		BufferedImage icon = null;
		try {
			icon = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/firesprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		gamePanel.frame.setIconImage(icon);
		
		gamePanel.setFocusable(true);
		gamePanel.frame.getContentPane().add(gamePanel,BorderLayout.CENTER);
		gamePanel.frame.pack();
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		if(!isTesting) {
			gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
    	gamePanel.startGameThread();
	}
}
