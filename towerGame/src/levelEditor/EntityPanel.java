package levelEditor;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import entity.Decoration;
import levelEditor.tool.Tool;

@SuppressWarnings("serial")
public class EntityPanel extends EditorPanel {

	public EntityPanel(LevelEditor le) {
		super(le);
		
		BufferedImage iconAddDecoration;
		try {
			iconAddDecoration = ImageIO.read(LevelEditor.class.getResourceAsStream("/sprites/levelEditor/AddDecoration.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iconAddDecoration = null;
		}
		JPanel livingPanel = new JPanel(), mapPanel = new JPanel(), settingsPanel = new JPanel();
		
		livingPanel.setPreferredSize(new Dimension(190, 115));
		livingPanel.setBorder(BorderFactory.createTitledBorder("Living"));

		mapPanel.setPreferredSize(new Dimension(190, 115));
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));

		settingsPanel.setPreferredSize(new Dimension(190, 200));
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Custom sprite editor"));
		
		this.add(livingPanel);
		this.add(mapPanel);
		this.add(settingsPanel);
		
		LevelEditorUtils.addButton("Entity;0", LevelEditor.iconFireEnemy, true, "Fire Enemy", livingPanel);

		LevelEditorUtils.addButton("Entity;1", LevelEditor.iconFireEnemyBlue, true, "Blue Fire Enemy", livingPanel);

		LevelEditorUtils.addButton("Entity;2", LevelEditor.iconThing, true, "Thing", livingPanel);

		LevelEditorUtils.addButton("Entity;5", LevelEditor.iconFlameDemon, true, "Flame Demon", livingPanel);

		LevelEditorUtils.addButton("Entity;6", LevelEditor.iconPuddleMonster, true, "Puddle Monster", livingPanel);

		LevelEditorUtils.addButton("Entity;7", LevelEditor.iconZombieKnight, true, "Zombie Knight", livingPanel);
		
		LevelEditorUtils.addButton("Entity;3", LevelEditor.iconManaOrb, true, "Mana Orb", mapPanel);

		LevelEditorUtils.addButton("Entity;4", LevelEditor.iconPlatform, true, "Floating Platform", mapPanel);

		LevelEditorUtils.addButton("Entity;8", LevelEditor.iconBomb, true, "Bomb", mapPanel);

		LevelEditorUtils.addButton("Add Decoration", iconAddDecoration, true, "Add Decoration", mapPanel);
		

		LevelEditor.addAction("Entity", (args) -> {
			if(args.length > 1)
				LevelEditor.gamePanel.drawEntity = Integer.valueOf(args[1]);
			LevelEditor.gamePanel.tool = Tool.addEntity;
		});
		
		LevelEditor.addAction("Add Decoration", (args) -> {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				BufferedImage decorationImage;
				try {
					decorationImage = ImageIO.read(new File(fc.getSelectedFile().getPath()));
					LevelEditor.placeableDecoration = new Decoration(LevelEditor.gamePanel.level, decorationImage);
					LevelEditor.gamePanel.tool = Tool.placeDecoration;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	

}
