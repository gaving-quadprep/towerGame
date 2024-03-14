package levelGenerator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import map.Level;

public class LevelGenerator {
	JFrame frame;
	public void generate(Level level, int background, int[][] pattern) {
		
	}
	public LevelGenerator() {
		frame = new JFrame();
		JPanel panel = new JPanel();
		frame.add(panel);
		frame.setVisible(true);
	}
}
