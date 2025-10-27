package levelEditor.panel;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import entity.Decoration;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.tool.Tool;
import main.Main;

@SuppressWarnings("serial")
public class EntityPanel extends EditorPanel {

	public EntityPanel(LevelEditor le) {
		super(le);
		
		BufferedImage iconAddDecoration = LevelEditorUtils.readImage("/sprites/levelEditor/AddDecoration.png");
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		JPanel livingPanel = new JPanel(), mapPanel = new JPanel(), settingsPanel = new JPanel();
		
		livingPanel.setBorder(BorderFactory.createTitledBorder("Living"));
		mapPanel.setBorder(BorderFactory.createTitledBorder("Map"));
		settingsPanel.setBorder(BorderFactory.createTitledBorder("Entity settings"));
		
		this.add(livingPanel);
		LevelEditorUtils.addSpacer(this, true, 4);
		this.add(mapPanel);
		LevelEditorUtils.addSpacer(this, true, 4);
		this.add(settingsPanel);
		
		LevelEditorUtils.addButton("Entity;0", LevelEditor.iconFireEnemy, true, "Fire Enemy", livingPanel);

		LevelEditorUtils.addButton("Entity;1", LevelEditor.iconFireEnemyBlue, true, "Blue Fire Enemy", livingPanel);

		LevelEditorUtils.addButton("Entity;2", LevelEditor.iconThing, true, "Thing", livingPanel);

		LevelEditorUtils.addButton("Entity;5", LevelEditor.iconFlameDemon, true, "Flame Demon", livingPanel);

		LevelEditorUtils.addButton("Entity;6", LevelEditor.iconPuddleMonster, true, "Puddle Monster", livingPanel);

		LevelEditorUtils.addButton("Entity;7", LevelEditor.iconZombieKnight, true, "Zombie Knight", livingPanel);

		LevelEditorUtils.addButton("Entity;9", LevelEditor.iconBombGuy, true, "Bomb Guy", livingPanel);
		
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
			BufferedImage decorationImage;
			try {
				decorationImage = ImageIO.read(new File(Main.promptFile()));
				LevelEditor.placeableDecoration = new Decoration(LevelEditor.gamePanel.level, decorationImage);
				LevelEditor.gamePanel.tool = Tool.placeDecoration;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				JOptionPane.showMessageDialog(LevelEditor.gamePanel, "erm that not an image");
			}
		});
	}

	public String getName() {
		return "Entity";
	}
	
	public String getIcon() {
		return "/sprites/enemy/redfiresprite.png";
	}
	
}
