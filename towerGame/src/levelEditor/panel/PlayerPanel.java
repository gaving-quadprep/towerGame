package levelEditor.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import levelEditor.layout.SpringUtilities;
import levelEditor.LevelEditor;
import levelEditor.LevelEditorUtils;
import levelEditor.WeaponComboBox;

@SuppressWarnings("serial")
public class PlayerPanel extends EditorPanel {

	public static final int START_POS = 1;
	public static final int HEALTH = 2;
	public static final int MANA = 4;
	public static final int WEAPON = 8;
	public static final int SPEED = 16;
	public static final int HEAL_PLAYER = 32;
	public static final int PLAYER_SPRITE = 64;
	public static final int ALL = 127;
	
	BufferedImage playerSprite;
	public static BufferedImage defaultPlayerSprite = LevelEditorUtils.readImage("/sprites/player.png");
	
	JButton playerButton;
	JTextField xInput = new JTextField(), yInput = new JTextField(), healthInput = new JTextField(), manaInput = new JTextField(), speedInput = new JTextField();
	JComboBox weaponInput = new WeaponComboBox();
	JCheckBox healPlayer;
	
	public PlayerPanel(final LevelEditor le) {
		super(le);
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
		
		JLabel label = new JLabel("Player Settings");
		label.setAlignmentX(Box.CENTER_ALIGNMENT);
		this.add(label);

		LevelEditorUtils.addSpacer(this, true, 12);
		
		playerButton = LevelEditorUtils.addButton("PlayerPanelChooseSprite", defaultPlayerSprite.getScaledInstance(48, 48, Image.SCALE_REPLICATE), false, this);
		playerButton.setAlignmentX(Box.CENTER_ALIGNMENT);
		
		LevelEditorUtils.addSpacer(this, true, 32);
		
		JPanel playerStatsPanel = new JPanel();
		playerStatsPanel.setLayout(new SpringLayout());

		xInput.setText(String.valueOf(le.level.playerStartX));
		LevelEditorUtils.addWithLabel(playerStatsPanel, xInput, "Start X:");
		yInput.setText(String.valueOf(le.level.playerStartY));
		LevelEditorUtils.addWithLabel(playerStatsPanel, yInput, "Start Y:");
		healthInput.setText(String.valueOf(LevelEditor.playerHealth));
		LevelEditorUtils.addWithLabel(playerStatsPanel, healthInput, "Health:");
		manaInput.setText(String.valueOf(LevelEditor.playerMana));
		LevelEditorUtils.addWithLabel(playerStatsPanel, manaInput, "Mana:");
		speedInput.setText(String.valueOf(LevelEditor.playerSpeed));
		LevelEditorUtils.addWithLabel(playerStatsPanel, speedInput, "Speed:");

		//weaponInput.setSelectedItem("Staff");
		LevelEditorUtils.addWithLabel(playerStatsPanel, weaponInput, "Weapon:");
		
		SpringUtilities.makeCompactGrid(playerStatsPanel, 6, 2, 4, 4, 4, 4);
		playerStatsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
		
		this.add(playerStatsPanel);
		
		healPlayer = new JCheckBox("Heal player");
		healPlayer.setSelected(true);
		healPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(healPlayer);

		this.add(Box.createVerticalGlue());
		
		LevelEditorUtils.addButton("PlayerPanelSaveChanges", "Apply", this).setAlignmentX(Box.CENTER_ALIGNMENT);
		
		
		LevelEditor.addAction("PlayerPanelChooseSprite", new LevelEditor.Action() {
			@Override
			public void run(String[] args) {
				JFileChooser fc = new JFileChooser();
				//fc.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
				int returnVal = fc.showOpenDialog(LevelEditor.gamePanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					BufferedImage image;
					try {
						image = ImageIO.read(new File(fc.getSelectedFile().getPath()));
						playerSprite = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
						playerSprite.getGraphics().drawImage(LevelEditorUtils.makeUnindexed(image), 0, 0, 16, 16, null);
						
						playerButton.setIcon(new ImageIcon(playerSprite.getScaledInstance(48, 48, Image.SCALE_REPLICATE)));
						
					} catch (Exception e) {
						e.printStackTrace();
						// Main.hamburger();
					}
				}
			}
		});
		
		LevelEditor.addAction("PlayerPanelSaveChanges", new LevelEditor.Action() {
			@Override
			public void run(String[] args) {
			LevelEditor.customSprites.remove("player.png");
			if(playerSprite != null)
				LevelEditor.customSprites.put("player.png", playerSprite);
			le.level.playerStartX = Double.valueOf(xInput.getText());
			le.level.playerStartY = Double.valueOf(yInput.getText());
			LevelEditor.playerHealth = Double.valueOf(healthInput.getText());
			LevelEditor.playerMana = Double.valueOf(manaInput.getText());
			LevelEditor.playerSpeed = Double.valueOf(speedInput.getText());
			LevelEditor.playerWeapon = weaponInput.getSelectedIndex();
			le.level.healPlayer = healPlayer.isSelected();
			}
		});
	}
	
	public void updateValues(int toUpdate) {
		if((toUpdate & START_POS) > 0) {
			xInput.setText(String.valueOf(le.level.playerStartX));
			yInput.setText(String.valueOf(le.level.playerStartY));
		}
		if((toUpdate & HEALTH) > 0)
			healthInput.setText(String.valueOf(LevelEditor.playerHealth));
		if((toUpdate & MANA) > 0)
			manaInput.setText(String.valueOf(LevelEditor.playerMana));
		if((toUpdate & SPEED) > 0)
			speedInput.setText(String.valueOf(LevelEditor.playerSpeed));

		if((toUpdate & PLAYER_SPRITE) > 0) {
			BufferedImage sprite = LevelEditor.customSprites.get("player.png");
			playerSprite = sprite;
			if (sprite == null)
				sprite = defaultPlayerSprite;
			playerButton.setIcon(new ImageIcon(sprite.getScaledInstance(48, 48, Image.SCALE_REPLICATE)));
		}
		if((toUpdate & WEAPON) > 0) {
			weaponInput.setSelectedIndex(LevelEditor.playerWeapon);
		}
		if((toUpdate & HEAL_PLAYER) > 0)
			healPlayer.setSelected(le.level.healPlayer);
	}
	
	public String getName() {
		return "Player";
	}
	
	public String getIcon() {
		return "/sprites/player.png";
	}

}
