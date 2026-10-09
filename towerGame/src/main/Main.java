package main;

import java.math.BigDecimal;
import java.util.Random;

import javax.swing.JPanel;
import levelEditor.LevelEditor;
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
	public static final String progLevelName = ".progress.tgl";

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
