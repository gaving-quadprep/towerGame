package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import towerGame.TowerGame;
import util.BaseEventHandler;

public abstract class Main {
	
	public static final BigDecimal ONE_TENTH = BigDecimal.valueOf(0.1);
	
	public static int frames = 0;
	public static int fpsCap = 60;
	public static int scale = 3;
	public static float zoom = 1;
	public static int tileSize = (int) ((16*zoom)*scale);
	public static int screenWidth = 320 * scale;
	public static int screenHeight = 240 * scale;
	public static int width = (int) Math.ceil((double) (screenWidth / tileSize));
	public static int height = (int) Math.ceil((double) (screenHeight / tileSize));
	public static final String version = "0.6.5";
	
	public static final WorldRenderer worldRenderer = new WorldRenderer();
	private static JFrame frame;
	private static JButton darkModeButton;
	public static JPanel currentGamePanel;
	static BaseEventHandler eventHandler;

	static String[] args;
	
	public static Random random = new Random();
	
	static {
		random.setSeed(System.currentTimeMillis());
	}
	
	private static class DisplayableLAFInfo extends LookAndFeelInfo {

		public DisplayableLAFInfo(LookAndFeelInfo lafInfo) {
			super(lafInfo.getName(), lafInfo.getClassName());
		}

		public DisplayableLAFInfo(String name, String className) {
			super(name, className);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public String toString() {
			return this.getName();
		}
		
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
		Main.args=args;
		List<DisplayableLAFInfo> themes = new ArrayList<DisplayableLAFInfo>();
		themes.add(new DisplayableLAFInfo("FlatLaf Light", FlatLightLaf.class.getCanonicalName()));
		themes.add(new DisplayableLAFInfo("FlatLaf Dark", FlatDarkLaf.class.getCanonicalName()));
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				themes.add(new DisplayableLAFInfo(info));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(args.length > 0) {
			System.gc();
			currentGamePanel=TowerGame.gamePanel;
			TowerGame.main(args);
			return;
		}
		
		MainActionListener m = new MainActionListener();
		
		frame = new JFrame("TowerQuest v"+version);
		frame.pack();
		frame.setSize(230,230);
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.add(panel);
		
		JLabel versionLabel = new JLabel("Welcome to TowerQuest v"+version);
		versionLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		LevelEditorUtils.addSpacer(panel, true, 5);
		panel.add(versionLabel);
		
		JButton levelButton = new JButton("Play a Level");
		levelButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		levelButton.addActionListener(m);

		LevelEditorUtils.addSpacer(panel, true, 5);
		panel.add(levelButton);
		
		JButton editorButton = new JButton("Launch Level Editor");
		editorButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		editorButton.addActionListener(m);

		LevelEditorUtils.addSpacer(panel, true, 5);
		panel.add(editorButton);

		
		JPanel scalePanel = new JPanel();
		scalePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		scalePanel.setLayout(new FlowLayout());
		
		JLabel scaleLabel = new JLabel("Window scale:");
		scalePanel.add(scaleLabel);
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
		scalePanel.add(spinner);
		panel.add(scalePanel);
		
		
		JPanel themePanel = new JPanel();
		themePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		themePanel.setLayout(new FlowLayout());
		
		JLabel themeLabel = new JLabel("Theme:");
		themePanel.add(themeLabel);
		
		JComboBox<DisplayableLAFInfo> cb = new JComboBox<DisplayableLAFInfo>(themes.toArray(new DisplayableLAFInfo[0]));
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplayableLAFInfo theme = (DisplayableLAFInfo)cb.getSelectedItem();
				try {
					String className = theme.getClassName();
					UIManager.setLookAndFeel(className);
					if(className.contains("flatlaf")) {
						JFrame.setDefaultLookAndFeelDecorated(true);
						JDialog.setDefaultLookAndFeelDecorated(true);
					} else {
						JFrame.setDefaultLookAndFeelDecorated(false);
						JDialog.setDefaultLookAndFeelDecorated(false);
						
					}
					SwingUtilities.updateComponentTreeUI(frame);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		themePanel.add(cb);
		panel.add(themePanel);
		
		BufferedImage icon = null;
		try {
			icon = ImageIO.read(Main.class.getResourceAsStream("/sprites/firesprite.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		frame.setIconImage(icon);
		
		frame.setVisible(true);

		// call after making it visible
		if( SystemInfo.isLinux ) {
			// enable custom window decorations
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		}
		
		return;
	}
}
