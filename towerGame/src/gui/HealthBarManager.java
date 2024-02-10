package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import entity.LivingEntity;
import main.Main;
import map.Level;

public class HealthBarManager extends GUI {
    public MainHealthBarManager mhb = new MainHealthBarManager();
    
    private double prevHealth = -1;
    private double prevMana = -1;
    
    public static Color manaBarColor = new Color(44, 70, 252);
    public class MainHealthBarManager {
	    public double health = 5;
	    public double mana = 6f;
	    public double maxHealth = 10;
	    public int hBarWidth = 50 * Main.scale;
	    public int hBarHeight = 5 * Main.scale;
	    public double minimumHealthForChangedAppearance = 3;
	    BufferedImage img = new BufferedImage(hBarWidth,hBarHeight*2, BufferedImage.TYPE_4BYTE_ABGR);
	    Graphics2D grphx = (Graphics2D)img.getGraphics();
    }
    public void refresh() {
    	prevHealth=-1;
    	prevMana=-1;
    }
    
    public static Color getColorFromHealth(double health, double maxHealth) {
    	double h = health / maxHealth;
    	return h < .25 ? Color.RED : h < .35 ? Color.ORANGE : h < .5 ? Color.YELLOW : Color.GREEN;
    }

    public void render(Graphics2D g2, Level level){
    	double h = level.player.health;
    	double m = level.player.mana;
        if((prevHealth != h || prevMana != m) && g2 != null){
	        //mhb.grphx = (Graphics2D)mhb.img.getGraphics();
	        mhb.grphx.setComposite(AlphaComposite.Clear);
	        mhb.grphx.fillRect(0, 0, mhb.hBarWidth, mhb.hBarHeight+mhb.hBarHeight);
	        mhb.grphx.setComposite(AlphaComposite.SrcOver);
	        mhb.grphx.setPaint(getColorFromHealth(h, 10));
	        mhb.grphx.setStroke(new BasicStroke(1.0f));
	        if(h > mhb.minimumHealthForChangedAppearance){
		        mhb.grphx.fillRect(0, 0, (int)(h*(mhb.hBarWidth/mhb.maxHealth)), mhb.hBarHeight);
		        mhb.grphx.setColor(Color.BLACK);
		        mhb.grphx.setPaint(Color.BLACK);
		        mhb.grphx.drawRect(0, 0, mhb.hBarWidth-1, mhb.hBarHeight-1);
	        }else{
		        mhb.grphx.fillRect(0, 0, (int)(h*10), mhb.hBarHeight);
		        mhb.grphx.setPaint(Color.BLACK);
		        
		        for(int i = 0;i<(h>=0.5?(h*2)-1:0);i++){
		            mhb.grphx.drawLine(i*5, mhb.hBarHeight, (i*5)+5, 0);
		        }
		        mhb.grphx.setColor(Color.BLACK);
		        mhb.grphx.setPaint(Color.BLACK);
		        mhb.grphx.drawRect(0, 0, mhb.hBarWidth-1, mhb.hBarHeight-1);
		        mhb.grphx.setColor(Color.WHITE);
		        mhb.grphx.setPaint(Color.WHITE);
	        }
	
	
	        mhb.grphx.setFont(new Font("Serif", Font.PLAIN, 12));
	        mhb.grphx.drawString(String.valueOf(Math.floor(h*10.0)/10), h >= 2.5 ? ((int)(h* 5 * Main.scale)-22) : 3, 9);
	        
	        mhb.grphx.setPaint(manaBarColor);
	        mhb.grphx.setStroke(new BasicStroke(1.0f));
	        mhb.grphx.fillRect(0, mhb.hBarHeight, (int)(m*(mhb.hBarWidth/15.0)), mhb.hBarHeight);
	        mhb.grphx.setColor(Color.BLACK);
	        mhb.grphx.setPaint(Color.BLACK);
	        mhb.grphx.drawRect(0, mhb.hBarHeight, mhb.hBarWidth-1, mhb.hBarHeight-1);
	        
	
	        mhb.grphx.setColor(Color.WHITE);
	        mhb.grphx.setPaint(Color.WHITE);
	        mhb.grphx.setFont(new Font("Serif", Font.PLAIN, 12));
	        mhb.grphx.drawString(String.valueOf(Math.round(m*10.0D)/10.0F), m >= 4.1 ? ((int)((m/15)*mhb.hBarWidth))-22 : 3, 9+mhb.hBarHeight);
        }
        if(g2 != null){
	        g2.drawImage(mhb.img, 0, 0, null);
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
				    c.setPaint(getColorFromHealth(le.health, le.maxHealth));  
				    int[] positions = e.getPositionOnScreen();
				    c.fillRect((positions[0])-(((int)((le.health/le.maxHealth)*(20*Main.scale))-(Main.tileSize))/2), Math.abs(positions[1] - 7*Main.scale)==(positions[1] - 7*Main.scale) ? (positions[1] - 7*Main.scale) : (positions[1] + 7*Main.scale), (int)((le.health/le.maxHealth)*(20*Main.scale)), 3*Main.scale);
		        }
		        
	        }
        }
    }
}
