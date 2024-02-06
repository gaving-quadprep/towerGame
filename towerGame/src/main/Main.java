package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import levelEditor.LevelEditor;
import towerGame.TowerGame;

public class Main {
	public static int scale=2;
	public static int tileSize=16*scale;
	public static int frames = 0;
	public static int fpsCap = 60;
	static String[] args;
	static JFrame frame;
	public static JPanel currentGamePanel;
	static class MainActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "Launch TowerGame") {
				String[] list = new String[1];
				JFileChooser fc = new JFileChooser();
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
		}
	}
	public static void main(String[] args) {
		Main.args=args;
		frame = new JFrame("TowerGame v0.1");
		frame.pack();
		frame.setSize(240,160);
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.add(panel);
		JLabel tf = new JLabel("Welcome to TowerGame v0.1");
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
		SpinnerModel spinnerModel = new SpinnerNumberModel(2, //initial value
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
