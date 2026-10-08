package main;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;

import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import towerGame.TowerGame;

public class TowerGameLauncher extends JFrame {

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
			return getName();
		}

	}
	private static class LauncherActionListener implements ActionListener {
		TowerGameLauncher parent;
		LauncherActionListener(TowerGameLauncher parent){
			this.parent = parent;
		}
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
				parent.dispose();
				System.gc();
				Main.currentGamePanel=TowerGame.gamePanel;
				TowerGame.main(list);
			}
			if(e.getActionCommand() == "Launch Level Editor") {
				parent.dispose();
				System.gc();
				Main.currentGamePanel=LevelEditor.gamePanel;
				LevelEditor.start(Main.args);
			}
		}
	}
	public TowerGameLauncher() {
		super("TowerQuest v"+Main.version);
		List<DisplayableLAFInfo> themes = new ArrayList<>();
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

		if(Main.args.length > 0) {
			System.gc();
			Main.currentGamePanel=TowerGame.gamePanel;
			TowerGame.main(Main.args);
			return;
		}

		LauncherActionListener l = new LauncherActionListener(this);

		pack();
		setSize(230,230);
		//frame.setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		add(panel);

		JLabel versionLabel = new JLabel("Welcome to TowerQuest v"+Main.version);
		versionLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		LevelEditorUtils.addSpacer(panel, true, 5);
		panel.add(versionLabel);

		JButton levelButton = new JButton("Play a Level");
		levelButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		levelButton.addActionListener(l);

		LevelEditorUtils.addSpacer(panel, true, 5);
		panel.add(levelButton);

		JButton editorButton = new JButton("Launch Level Editor");
		editorButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		editorButton.addActionListener(l);

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
		spinner.addChangeListener(e -> {
			Main.scale = (int) ((JSpinner)e.getSource()).getValue();
			Main.changeScale(Main.scale);
		});
		scalePanel.add(spinner);
		panel.add(scalePanel);


		JPanel themePanel = new JPanel();
		themePanel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		themePanel.setLayout(new FlowLayout());

		JLabel themeLabel = new JLabel("Theme:");
		themePanel.add(themeLabel);

		JComboBox<DisplayableLAFInfo> cb = new JComboBox<>(themes.toArray(new DisplayableLAFInfo[0]));
		cb.addActionListener(e -> {
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
				SwingUtilities.updateComponentTreeUI(this);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
		setIconImage(icon);

		setVisible(true);

		// call after making it visible
		if( SystemInfo.isLinux ) {
			// enable custom window decorations
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
		}

		return;
	}
}
