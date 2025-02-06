package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import levelEditor.LevelEditor;
import towerGame.TowerGame;
import util.BaseEventHandler;

//test to see if i set up branches properly

public abstract class Main {
	
	public static final BigDecimal ONE_TENTH = BigDecimal.valueOf(0.1);
	
	public static int frames = 0;
	public static int fpsCap = 60;
	public static int scale = 2;
	public static float zoom = 1;
	public static int tileSize = (int) ((16*zoom)*scale);
	public static int screenWidth = 320 * scale;
	public static int screenHeight = 240 * scale;
	public static int width = (int) Math.ceil((double) (screenWidth / tileSize));
	public static int height = (int) Math.ceil((double) (screenHeight / tileSize));
	public static final String version = "0.6.4";
	
	public static final WorldRenderer worldRenderer = new WorldRenderer();
	private static JFrame frame;
	public static JPanel currentGamePanel;
	static BaseEventHandler eventHandler;

	static String[] args;
	
	public static Random random = new Random();
	
	static {
		random.setSeed(System.currentTimeMillis());
	}
	
	public static void changeScale(int scale) {
		Main.scale = scale;
		tileSize = (int) ((16*zoom)*scale);
		screenWidth = 320 * scale;
		screenHeight = 240 * scale;
		width = (int)Math.ceil((double) (screenWidth / tileSize));
		height = (int)Math.ceil((double) (screenHeight / tileSize));
		if(zoom <= 1) {
			if(TowerGame.isRunning())
				TowerGame.gamePanel.level.rescaleTiles();
			if(LevelEditor.gamePanel != null)
				LevelEditor.gamePanel.level.rescaleTiles();
		}
	}
	public static void changeZoom(float zoom) {
		Main.zoom = zoom;
		tileSize = (int) ((16*zoom)*scale);
		width = (int)Math.ceil((double) (screenWidth / tileSize));
		height = (int)Math.ceil((double) (screenHeight / tileSize));
		if(zoom <= 1) {
			if(TowerGame.isRunning())
				TowerGame.gamePanel.level.rescaleTiles();
			if(LevelEditor.gamePanel != null)
				LevelEditor.gamePanel.level.rescaleTiles();
		}
	}
	static class MainActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "Play a Level") {
				String[] list = new String[1];
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(
						"TowerQuest Level", "tgl"));
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					list[0] = fc.getSelectedFile().getPath();
				}else {
					return;
				}
				frame.dispose();
				System.gc();
				currentGamePanel=TowerGame.gamePanel;
				TowerGame.main(list);
			}
			if(e.getActionCommand() == "Launch Level Editor") {
				frame.dispose();
				System.gc();
				currentGamePanel=LevelEditor.gamePanel;
				LevelEditor.start(args);
			}
		}
	}
	public static void main(String[] args) {
		Main.args = args;

		if(args.length > 0) {
			System.gc();
			currentGamePanel=TowerGame.gamePanel;
			TowerGame.main(args);
			return;
		}
		
		MainActionListener m = new MainActionListener();
		
		frame = new JFrame("TowerQuest v"+version);
		frame.pack();
		frame.setSize(230,192);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.add(panel);
		
		JLabel tf = new JLabel("Welcome to TowerQuest v"+version);
		panel.add(tf);
		
		JButton button = new JButton("Play a Level");
		button.addActionListener(m);
		
		panel.add(button);
		
		JButton button2 = new JButton("Launch Level Editor");
		button2.addActionListener(m);
		
		panel.add(button2);

		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout());
		
		JLabel sc = new JLabel("Window scale:");
		panel2.add(sc);
		SpinnerModel spinnerModel = new SpinnerNumberModel(3, //initial value
				 1, //min
				 16, //max
				 1);//step
		JSpinner spinner = new JSpinner(spinnerModel);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scale = (int) ((JSpinner)e.getSource()).getValue();
				changeScale(scale);
			}
		});
		panel2.add(spinner);
		panel.add(panel2);

		
		BufferedImage icon = null;
		try {
			icon = ImageIO.read(Main.class.getResourceAsStream("/sprites/firesprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		frame.setIconImage(icon);
		
		frame.setVisible(true);
		return;
	}
}
