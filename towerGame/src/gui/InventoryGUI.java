package gui;

import java.awt.Graphics2D;

import item.Item;
import main.Main;
import map.Level;

public class InventoryGUI extends GUI {

	@Override
	public void render(Graphics2D g2, Level level) {
		level.player.inventory[0] = new Item();
		level.player.inventory[0].sprite = level.getSprite(level.player.inventory[0].getSprite());
		g2.setColor(GUI.backgroundColor);
		g2.fillRect(20*Main.scale, 20*Main.scale, 320*Main.scale-40*Main.scale, 240*Main.scale-40*Main.scale);
		for(int w=0;w<5;w++) {
			for (int h=0;h<3;h++) {
				Item i = level.player.inventory[h*5+w];
				if(i != null)
					g2.drawImage(i.sprite, 30*Main.scale+36*w*Main.scale, 30*Main.scale+36*h*Main.scale, 32*Main.scale, 32*Main.scale, null);
			}
		}
	}

}
