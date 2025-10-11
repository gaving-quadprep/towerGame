package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.LivingEntity;
import main.Main;
import map.Level;
import util.PixelPosition;

public class HealthBarManager extends GUI {
	
	private double prevHealth = -1;
	private double prevMana = -1;
	
	// this is done to avoid creating a color object for every entity every frame
	public static final Color darkRed = Color.red.darker();
	public static final Color darkOrange = Color.orange.darker();
	public static final Color darkYellow = Color.yellow.darker();
	public static final Color darkGreen = Color.green.darker();
	
	public Color manaBarColor = new Color(54, 80, 252);
	public int hBarWidth = 75 * Main.scale;
	public int hBarHeight = 8 * Main.scale;
	public int cHBarHeight = 3 * Main.scale;
	public int cHBarWidth = 20 * Main.scale;
	public static final double minimumHealthForChangedAppearance = 3;
	BufferedImage img = new BufferedImage(hBarWidth, hBarHeight*2, BufferedImage.TYPE_4BYTE_ABGR);
	Graphics2D grphx = (Graphics2D)img.getGraphics();
	
	public void refresh() {
		prevHealth = -1;
		prevMana = -1;
	}

	public static Color getColorFromHealth(double health, double maxHealth) {
		double h = health / maxHealth;
		return h <= .2 ? Color.RED : h < .35 ? Color.ORANGE : h < .5 ? Color.YELLOW : Color.GREEN;
	}
	public static Color getDarkColorFromHealth(double health, double maxHealth) {
		double h = health / maxHealth;
		return h <= .2 ? darkRed : h < .35 ? darkOrange : h < .5 ? darkYellow : darkGreen;
	}

	public void render(Graphics2D g2, Level level){
		double h = level.player.health.doubleValue();
		double m = level.player.mana.doubleValue();
		if((prevHealth != h || prevMana != m) && g2 != null) {
			grphx.setComposite(AlphaComposite.Clear);
			grphx.fillRect(0, 0, hBarWidth, hBarHeight+hBarHeight);
			grphx.setComposite(AlphaComposite.SrcOver);
			
			grphx.setPaint(getColorFromHealth(h, 10));
			grphx.setStroke(new BasicStroke(1.0f));
			int pixWidth = (int)(h*(hBarWidth/level.player.maxHealth.doubleValue()));
			grphx.fillRect(0, 0, pixWidth, hBarHeight);
			
			if(h <= minimumHealthForChangedAppearance){
				grphx.setPaint(getColorFromHealth(h, 10).darker());
				
				for(int i = 0;i < pixWidth / 5;i++){
					grphx.drawLine(i*5, hBarHeight, (i*5)+5, 0);
				}
			}
			
			grphx.setColor(Color.BLACK);
			grphx.setPaint(Color.BLACK);
			grphx.drawRect(0, 0, hBarWidth-1, hBarHeight-1);
			grphx.setColor(Color.WHITE);
			grphx.setPaint(Color.WHITE);
	
			GUI.fontRenderer.drawText(grphx, String.valueOf(Math.floor(h*10.0)/10), Math.max((int)((h/10)*hBarWidth)-14*Main.scale, 3), 0);
			
			grphx.setPaint(manaBarColor);
			grphx.setStroke(new BasicStroke(1.0f));
			grphx.fillRect(0, hBarHeight, (int)(m*(hBarWidth/15.0)), hBarHeight);
			grphx.setColor(Color.BLACK);
			grphx.setPaint(Color.BLACK);
			grphx.drawRect(0, hBarHeight, hBarWidth-1, hBarHeight-1);
			
			GUI.fontRenderer.drawText(grphx, String.valueOf(Math.floor(m*10.0)/10), Math.max((int)((m/15)*hBarWidth)-14*Main.scale, 3), hBarHeight);
		}
		if(g2 != null){
			g2.drawImage(img, 0, 0, null);
			renderSprites(g2, level);
		}
		
		prevHealth = h;
		prevMana = m;
	}

	public void renderSprites(final Graphics2D g2, Level lvl){
		lvl.forEachEntityOfType(LivingEntity.class, false, new Level.EntityIterator<LivingEntity>() {
			@Override
			public void forEach(LivingEntity le) {
				if(le.shouldRenderHealthBar) {
					g2.setColor(Color.GREEN);
					double h = le.health.doubleValue();
					double mh = le.maxHealth.doubleValue();
					g2.setPaint(getColorFromHealth(h, mh));  
					PixelPosition position = Main.worldRenderer.positionToPixel(le.x, le.y);
					int x = (position.x)-(((cHBarWidth)-(Main.scale * le.getSpriteWidth()))/2);
					int y = (position.y - 7*Main.scale);
					if(le.invulnerable) {
						g2.setPaint(Color.BLUE);  
						g2.fillRect(x, y, cHBarWidth, cHBarHeight);
					} else {
						g2.setPaint(getDarkColorFromHealth(h, mh));  
						g2.fillRect(x, y, cHBarWidth, cHBarHeight);
						g2.setPaint(getColorFromHealth(h, mh));  
						g2.fillRect(x, y, (int)((h/mh) * cHBarWidth), cHBarHeight);
					}
				}
			}
		});
	}
}
