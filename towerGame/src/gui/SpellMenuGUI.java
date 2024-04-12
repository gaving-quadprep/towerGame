package gui;

import java.awt.Graphics2D;

import main.Main;
import map.Level;
import weapon.Spell;

public class SpellMenuGUI extends GUI {

	@Override
	public void render(Graphics2D g2, Level level) {
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(40*Main.scale, 30*Main.scale, 320*Main.scale-80*Main.scale, 240*Main.scale-60*Main.scale);
		for(Spell sp : level.player.spells) {
			
		}
	}

}
