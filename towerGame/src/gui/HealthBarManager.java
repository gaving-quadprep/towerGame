package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import entity.LivingEntity;
import main.Main;
import map.Level;

public class HealthBarManager extends GUI {
	
	private double prevHealth = -1;
	private double prevMana = -1;
	
	public Color manaBarColor = new Color(54, 80, 252);
	public int hBarWidth = 75 * Main.scale;
	public int hBarHeight = 8 * Main.scale;
	public static final double minimumHealthForChangedAppearance = 3;
	BufferedImage img = new BufferedImage(hBarWidth,hBarHeight*2, BufferedImage.TYPE_4BYTE_ABGR);
	Graphics2D grphx = (Graphics2D)img.getGraphics();
	
	public void refresh() {
		prevHealth = -1;
		prevMana = -1;
	}
	
	public static Color getColorFromHealth(double health, double maxHealth) {
		double h = health / maxHealth;
		return h <= .2 ? Color.RED : h < .35 ? Color.ORANGE : h < .5 ? Color.YELLOW : Color.GREEN;
	}

	public void render(Graphics2D g2, Level level){
		double h = level.player.health.doubleValue();
		double m = level.player.mana.doubleValue();
		if((prevHealth != h || prevMana != m) && g2 != null){
			grphx.setComposite(AlphaComposite.Clear);
			grphx.fillRect(0, 0, hBarWidth, hBarHeight+hBarHeight);
			grphx.setComposite(AlphaComposite.SrcOver);
			grphx.setPaint(getColorFromHealth(h, 10));
			grphx.setStroke(new BasicStroke(1.0f));
			if(h > minimumHealthForChangedAppearance){
				grphx.fillRect(0, 0, (int)(h*(hBarWidth/level.player.maxHealth.doubleValue())), hBarHeight);
				grphx.setColor(Color.BLACK);
				grphx.setPaint(Color.BLACK);
				grphx.drawRect(0, 0, hBarWidth-1, hBarHeight-1);
			}else{
				grphx.fillRect(0, 0, (int)(h*10), hBarHeight);
				grphx.setPaint(Color.BLACK);
				
				for(int i = 0;i<(h>=0.5?(h*2)-1:0);i++){
					grphx.drawLine(i*5, hBarHeight, (i*5)+5, 0);
				}
				grphx.setColor(Color.BLACK);
				grphx.setPaint(Color.BLACK);
				grphx.drawRect(0, 0, hBarWidth-1, hBarHeight-1);
				grphx.setColor(Color.WHITE);
				grphx.setPaint(Color.WHITE);
			}
	
			GUI.fontRenderer.drawText(grphx, String.valueOf(Math.floor(h*10.0)/10), h >= 2.5 ? ((int)((h/10)*hBarWidth)-14*Main.scale) : 3, 0);
			
			grphx.setPaint(manaBarColor);
			grphx.setStroke(new BasicStroke(1.0f));
			grphx.fillRect(0, hBarHeight, (int)(m*(hBarWidth/15.0)), hBarHeight);
			grphx.setColor(Color.BLACK);
			grphx.setPaint(Color.BLACK);
			grphx.drawRect(0, hBarHeight, hBarWidth-1, hBarHeight-1);
			
			GUI.fontRenderer.drawText(grphx, String.valueOf(Math.round(m*10.0D)/10.0F), m >= 4.1 ? ((int)((m/15)*hBarWidth))-14*Main.scale : 3, hBarHeight);
		}
		if(g2 != null){
			g2.drawImage(img, 0, 0, null);
			renderSprites(g2, level);
		}
		
		prevHealth = h;
		prevMana = m;
	}

	public void renderSprites(Graphics2D c, Level lvl){
		for(Entity e : lvl.entities){
			if(e instanceof LivingEntity){
				LivingEntity le = (LivingEntity)e;
				if(le.shouldRenderHealthBar) {
					c.setColor(Color.GREEN);
					double h = le.health.doubleValue();
					double mh = le.maxHealth.doubleValue();
					c.setPaint(getColorFromHealth(h, mh));  
					int[] positions = e.getPositionOnScreen();
					int x = (positions[0])-(((20*Main.scale)-(Main.scale*e.getSpriteWidth()))/2);
					int y = Math.abs(positions[1] - 7*Main.scale)==(positions[1] - 7*Main.scale) ? (positions[1] - 7*Main.scale) : (positions[1] + 7*Main.scale);
					if(le.invulnerable) {
						c.setPaint(Color.BLUE);  
						c.fillRect(x, y, 20*Main.scale, 3*Main.scale);
					} else {
						c.setPaint(getColorFromHealth(h, mh).darker());  
						c.fillRect(x, y, 20*Main.scale, 3*Main.scale);
						c.setPaint(getColorFromHealth(h, mh));  
						c.fillRect(x, y, (int)((h/mh)*(20*Main.scale)), 3*Main.scale);
					}
				}
			}
		}
	}
}
