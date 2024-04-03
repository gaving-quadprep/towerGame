package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;

import javax.imageio.ImageIO;
import javax.swing.JButton;
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

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import levelEditor.LevelEditor;
import towerGame.TowerGame;

public class Main {
	public static final BigDecimal ONE_TENTH = BigDecimal.valueOf(0.1);
	
	public static int scale=3;
	public static int tileSize=16*scale;
	public static int frames = 0;
	public static int fpsCap = 60;
	public static int height = 15;
	public static int width = 20;
	public static final String version = "0.5";
	static String[] args;
	private static JFrame frame;
	private static JButton darkModeButton;
	public static JPanel currentGamePanel;
	static class MainActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "Launch TowerGame") {
				String[] list = new String[1];
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter(
						"TowerGame Level", "tgl"));
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					list[0] = fc.getSelectedFile().getPath();
				}else {
					return;
				}
				frame.dispose();
				frame = null;
				System.gc();
				currentGamePanel=TowerGame.gamePanel;
				TowerGame.main(list);
			}
			if(e.getActionCommand() == "Launch TowerGame LevelEditor") {
				frame.dispose();
				frame = null;
				System.gc();
				currentGamePanel=LevelEditor.gamePanel;
				LevelEditor.main(args);
			}
			if(e.getActionCommand() == "Switch to Dark Mode") {
				try {
					UIManager.setLookAndFeel(new FlatDarkLaf());
					SwingUtilities.updateComponentTreeUI(frame);
					darkModeButton.setText("Switch to Light Mode");
					darkModeButton.setActionCommand("Switch to Light Mode");
					darkModeButton.repaint();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			if(e.getActionCommand() == "Switch to Light Mode") {
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
					SwingUtilities.updateComponentTreeUI(frame);
					darkModeButton.setText("Switch to Dark Mode");
					darkModeButton.setActionCommand("Switch to Dark Mode");
					darkModeButton.repaint();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		System.setProperty("prism.allowhidpi", "false");
		Main.args=args;
		// disable the following code to get the old theme
		try {
			/*for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}*/ 	
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		frame = new JFrame("TowerGame v"+version);
		frame.pack();
		frame.setSize(240,180);
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.add(panel);
		JLabel tf = new JLabel("Welcome to TowerGame v"+version);
		panel.add(tf);
		MainActionListener m = new MainActionListener();
		JButton button = new JButton("Launch TowerGame");
		button.addActionListener(m);
		panel.add(button);
		JButton button2 = new JButton("Launch TowerGame LevelEditor");
		button2.addActionListener(m);
		panel.add(button2);
		JLabel sc = new JLabel("GUI scale:");
		panel.add(sc);
		SpinnerModel spinnerModel = new SpinnerNumberModel(3, //initial value
				 1, //min
				 16, //max
				 1);//step
		JSpinner spinner = new JSpinner(spinnerModel);
		  spinner.addChangeListener(new ChangeListener() {
			  public void stateChanged(ChangeEvent e) {
				 scale = (int) ((JSpinner)e.getSource()).getValue();
				 tileSize = 16 * scale;
			  }
		   });
		panel.add(spinner);
		
		darkModeButton = new JButton("Switch to Dark Mode");
		darkModeButton.addActionListener(m);
		panel.add(darkModeButton);

		
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
