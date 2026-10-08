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
import javax.swing.SpinnerModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;
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
	public static int width = (int) Math.ceil(screenWidth / tileSize);
	public static int height = (int) Math.ceil(screenHeight / tileSize);
	public static final String version = "0.6.6";

	public static final WorldRenderer worldRenderer = new WorldRenderer();
	public static JPanel currentGamePanel;
	static TowerGameLauncher launcher;
	static BaseEventHandler eventHandler;

	static String[] args;

	public static Random random = new Random();

	static {
		random.setSeed(System.currentTimeMillis());
	}

	private static void updateScale() {
		width = (int)Math.ceil(screenWidth / tileSize);
		height = (int)Math.ceil(screenHeight / tileSize);
		if(zoom <= 1) {
			if(TowerGame.isRunning())
				TowerGame.gamePanel.level.rescaleTiles();
			if(LevelEditor.gamePanel != null)
				LevelEditor.gamePanel.level.rescaleTiles();
		}
	}
	public static void changeScale(int scale) {
		Main.scale = scale;
		tileSize = (int) ((16*zoom)*scale);
		screenWidth = 320 * scale;
		screenHeight = 240 * scale;
		updateScale();
	}
	public static void changeZoom(float zoom) {
		Main.zoom = zoom;
		tileSize = (int) ((16*zoom)*scale);
		updateScale();
	}
	public static void main(String[] args) {
		Main.args=args;
		launcher = new TowerGameLauncher();
	}
}
