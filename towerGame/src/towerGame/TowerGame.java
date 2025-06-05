package towerGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.GUI;
import gui.HealthBarManager;
import gui.PauseMenu;
import item.Item;
import levelEditor.LevelEditorUtils;
import main.Main;
import main.Renderer;
import map.Level;
import save.SaveFile;
import sound.SoundManager;

@SuppressWarnings("serial")
public class TowerGame extends JPanel implements Runnable {
	Thread gameThread;
	public JFrame frame;
	public static TowerGame gamePanel;
	EventHandler eventHandler = new EventHandler(frame);
	public Level level = new Level(0, 0, false);
	HealthBarManager hBarManager = new HealthBarManager();
	String filePath;
	public double remainingTime, drawStart, drawEnd, drawTime;
	public static double playerCheckpointX, playerCheckpointY;
	public static boolean hasWon;
	public ArrayList<GUI> guis = new ArrayList<GUI>();
	public static GUI pauseMenu = new PauseMenu();
	public static boolean isTesting;
	public static boolean loading = true
	private static boolean running = true;
	
	public TowerGame() {
		this.addKeyListener(eventHandler);
		this.addMouseListener(eventHandler);
		this.addMouseMotionListener(eventHandler);
		this.setPreferredSize(new Dimension(320*Main.scale,240*Main.scale));
		this.setDoubleBuffered(true);
		this.setBackground(Color.black);
	}
	public static boolean isRunning() {
		if(gamePanel != null)
			if(gamePanel.gameThread != null)
				return gamePanel.gameThread.isAlive();
		return false;
	}
	private void writeObject(ObjectOutputStream oos) throws IOException {
		throw new NotSerializableException();
	}
	public void update() {
		try {
			level.update(eventHandler);
		} catch(Exception e) {
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
	public static void showUnique(GUI gui) {
		hideAllOfType(gui.getClass());
		show(gui);
	}
	public static void hide(GUI gui) {
		gamePanel.guis.remove(gui);
	}
	public static boolean hideAllOfType(Class<? extends GUI> clazz) {
		return gamePanel.guis.removeIf((GUI g) -> clazz.isInstance(g));
	}
	public static void toggle(GUI gui) {
		if(!hideAllOfType(gui.getClass()))
			show(gui);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		if(Renderer.currentGraphicsConfiguration == null)
			Renderer.currentGraphicsConfiguration = g2.getDeviceConfiguration();

		if(level == null) {
			g2.setColor(Color.BLACK);
			g2.fillRect(0, 0, 320*Main.scale, 240*Main.scale);
			GUI.fontRenderer.drawTextCentered(g2, "Loading...", 160 * Main.scale, 120 * Main.scale);
			return;
		}
		g2.setColor(level.skyColor);
		g2.fillRect(0, 0, 320*Main.scale, 240*Main.scale);
		try {
			Main.worldRenderer.setGraphics(g2);
			level.render(Main.worldRenderer);
			if(level.player != null) {
				level.entity_lock.lock();
				try {
					for ( GUI gui : guis) {
						if(gui != null)
							gui.render(g2, level);
					}
				} finally {
					level.entity_lock.unlock();
				}
				
			}
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass()+": "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		if(loading)
			GUI.fontRenderer.drawTextCentered(g2, "Loading...", 160 * Main.scale, 120 * Main.scale);
		g2.dispose();
		
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void reloadLevel(boolean resetProgress) {
		try {
			loading = true;
			repaint();
			SaveFile.load(level, filePath);
		} catch (Exception e) {
			loading = false;
			level = new Level(20, 15);
			level.setPlayer(new Player(level));
		}
		Main.worldRenderer.level = level;
		hBarManager.refresh();
		level.player.xVelocity = 0;
		level.player.yVelocity = 0;
		if(eventHandler.shiftPressed) {
			playerCheckpointX = level.playerStartX;
			playerCheckpointY = level.playerStartY;
			level.player.inventory = new Item[15];
			Main.frames = 0;
		}
		level.player.x = playerCheckpointX;
		level.player.y = playerCheckpointY;
		level.centerCameraOnPlayer();
		level.needsToBeRedrawn = true;
		loading = false;
	}

	@Override
	public void run() {
		show(hBarManager);
		Player player = new Player(level);
		level.setPlayer(player);
		try {
			SaveFile.load(level, filePath);
			level.centerCameraOnPlayer();
			level.resizeImage();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Failed to load level: "+e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		Main.worldRenderer.level = level;
		playerCheckpointX = level.playerStartX;
		playerCheckpointY = level.playerStartY;
		loading = false;
		while (gameThread!=null && running) {
			double drawInterval = 1000000000/Main.fpsCap;
			double nextDrawTime=System.nanoTime()+drawInterval;

			
			
			drawStart = System.nanoTime();

			if(eventHandler.paused)
				nextDrawTime += drawInterval; // sleep for twice as long while paused to reduce cpu usage

			for(int i=0;i<(60 / Main.fpsCap);i++) {
				if(!eventHandler.paused) {
					update();
					Main.frames++;
				}
				if(level.player.health.compareTo(BigDecimal.ZERO) <= 0) {
				  reloadLevel(false);
				}


				if(eventHandler.resetPressed) {
					eventHandler.resetPressed = false;
				  reloadLevel(eventHandler.shiftPressed);
					loading = false;
				}
				
				if(hasWon) {
					JOptionPane.showMessageDialog(null, "You win!\nTime: "+String.format("%02.0f", Math.floor((float)Main.frames/3600))+":"+String.format("%05.2f", ((float)Main.frames)/60%60), "Congrats", JOptionPane.INFORMATION_MESSAGE);
					SoundManager.cleanUpSounds();
					gameThread.interrupt();
					if(!isTesting) {
						System.exit(0);
					}else {
						frame.dispose();
						running = false;
						gameThread.interrupt();
					}
					return;
				}
				
				if(eventHandler.mouse1Clicked) {
					eventHandler.mouse1Clicked=false;
				}
				if(eventHandler.mouse2Clicked) {
					eventHandler.mouse2Clicked=false;
				}
				
				repaint();
				
				
				try {
					remainingTime = (nextDrawTime-System.nanoTime()) / 1000000;
					if(remainingTime < 0) {
						remainingTime = 0;
					}
					Thread.sleep((long) remainingTime);
				} catch (InterruptedException e) {
					if(isTesting) {
						frame.dispose();
						SoundManager.cleanUpSounds();
						return;
					}
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: Failed to sleep thread", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		running = true;
		gamePanel = new TowerGame();
		if(args.length > 0) {
			gamePanel.filePath = args[0];
		}

		isTesting = (args.length > 1);
		
		gamePanel.frame = new JFrame("Tower Game");
		
		BufferedImage icon = LevelEditorUtils.readImage("/sprites/firesprite.png");
		gamePanel.frame.setIconImage(icon);
		
		gamePanel.setFocusable(true);
		gamePanel.frame.getContentPane().add(gamePanel,BorderLayout.CENTER);
		gamePanel.frame.pack();
		gamePanel.frame.setVisible(true);
		gamePanel.frame.setResizable(false);
		gamePanel.frame.setLocationRelativeTo(null);
		if(!isTesting) {
			gamePanel.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}else {
			gamePanel.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			
			JMenuBar menuBar = new JMenuBar();
			JMenuItem exit = new JMenuItem("Exit");
			class ExitListener implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					gamePanel.gameThread.interrupt();
					running = false;
					gamePanel.frame.dispose();
				}
				
			}
			exit.addActionListener(new ExitListener());
			menuBar.add(exit);
			gamePanel.frame.setJMenuBar(menuBar);
			gamePanel.frame.pack();
			
			gamePanel.frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent event) {
					gamePanel.gameThread.interrupt();
					running = false;
					gamePanel.frame.dispose();
				}
			});
		}
		gamePanel.startGameThread();
	}
}
