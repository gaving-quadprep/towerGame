// package towerGame;

// import java.awt.AlphaComposite;
// import java.awt.BasicStroke;
// import java.awt.Color;
// import java.awt.Font;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.Paint;
// import java.awt.image.BufferedImage;
// import java.awt.image.RescaleOp;
// import java.io.IOException;
// import java.nio.channels.FileLockInterruptionException;
// import java.util.List;

// import towerGame.map.Level;
// import towerGame.npc.Enemy;

// import java.util.ArrayList;




// public class HealthBarManager {
//     private float prevHealth = -1;
//     private float prevMana = -1;

//     public class MainHealthBarManager {
//     public float health = 2.1f;
//     public float mana = 0.2f;
//     public float maxHealth = 10;
//     public int hBarWidth = 100;
//     public int mBarWidth = 100;
//     public int hBarHeight = 10;
//     public int mBarHeight = 10;
//     public float minimumHealthForChangedAppearanceOfHealthBar = 3;
//     BufferedImage img = new BufferedImage(hBarWidth,hBarHeight+mBarHeight, BufferedImage.TYPE_4BYTE_ABGR);
//     Graphics2D grphx = (Graphics2D)img.getGraphics();


//     }

//     public class CharacterHealthBarManager {
//     public float health = 3.1f;
//     public float mana = 10;
//     public float maxHealth = 10;
//     public int hBarWidth = 100;
//     public int hBarHeight = 10;
//     public int Width = 640;
//     public int Height = 480;
//     public float minimumHealthForChangedAppearanceOfHealthBar = 3;
//     BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR);
//     Graphics2D grphx = (Graphics2D)img.getGraphics();


//     }
//     public MainHealthBarManager mhb;
//     public CharacterHealthBarManager chb;
//     public HealthBarManager(){
//         mhb = new MainHealthBarManager();
//         chb = new CharacterHealthBarManager();
//         render(null, 2.1f, 0.2f, null);
//         prevHealth = 10;
//     }

//     public void render(Graphics2D c, float h, float m, Level lvl){
//         if(prevHealth != h){
//         if(mhb.health > mhb.minimumHealthForChangedAppearanceOfHealthBar){
//         mhb.grphx.setPaint(Color.GREEN);
//         mhb.grphx.setStroke(new BasicStroke(1.0f));
//         mhb.grphx.fillRect(0, 0, (int)(h*10), mhb.hBarHeight);
//         mhb.grphx.setColor(Color.BLACK);
//         mhb.grphx.setPaint(Color.BLACK);
//         mhb.grphx.drawRect(0, 0, 99, mhb.hBarHeight-1);
//         }else{
//         mhb.grphx.setPaint(Color.RED);
//         mhb.grphx.setStroke(new BasicStroke(1.0f));
//         mhb.grphx.fillRect(0, 0, (int)(h*10), mhb.hBarHeight);
//         mhb.grphx.setPaint(Color.BLACK);
        
//         for(int i = 0;i<h*2;i++){
//             mhb.grphx.drawLine(i*5, mhb.hBarHeight, (i*5)+5, 0);
//         }
//         mhb.grphx.setColor(Color.BLACK);
//         mhb.grphx.setPaint(Color.BLACK);
//         mhb.grphx.drawRect(0, 0, 99, mhb.hBarHeight-1);
//         mhb.grphx.setColor(Color.WHITE);
//         mhb.grphx.setPaint(Color.WHITE);
//         }


//         mhb.grphx.setFont(new Font("Serif", Font.PLAIN, 12));
//         mhb.grphx.drawString(""+(Math.floor(h*10.0)/10), h >= 2.5 ? ((int)(h*10)-22) : 3, 9);
//     }
//         if(prevMana != m){
        
//         mhb.grphx.setPaint(Color.BLUE);
//         mhb.grphx.setStroke(new BasicStroke(1.0f));
//         mhb.grphx.fillRect(0, 10, (int)(m*10), mhb.mBarHeight);
//         mhb.grphx.setColor(Color.BLACK);
//         mhb.grphx.setPaint(Color.BLACK);
//         mhb.grphx.drawRect(0, 10, 99, mhb.mBarHeight-1);
        

//         mhb.grphx.setColor(Color.WHITE);
//         mhb.grphx.setPaint(Color.WHITE);
//         mhb.grphx.setFont(new Font("Serif", Font.PLAIN, 12));
//         mhb.grphx.drawString(""+(Math.floor(m*10.0)/10), m >= 2.5 ? ((int)(m*10)-22) : 3, 9+10);
//     }
//        if(lvl != null){
//         renderSprites(c, h, m, lvl);
//        } 
//         if(c != null){
//                 c.drawImage(chb.img, 0, 0, null);
//         c.drawImage(mhb.img, 0, 0, null);
//         }
//         prevHealth = h;
//         prevMana = m;
//     }

//     public void renderSprites(Graphics2D c, float h, float m, Level lvl){
//         renderThread t = new renderThread(c, h, m, lvl);
//         t.start();
        
//     }
//     private class renderThread extends Thread {
//         Graphics2D c;
//         float h;
//         float m;
//         Level lvl;
//         public renderThread(Graphics2D ci, float hi, float mi, Level lvli){
//             c = ci;
//             h = hi;
//             m = mi;
//             lvl = lvli;
//         }
//         public void run(){
//             try {
//         chb.grphx.setColor(Color.GREEN);
//         chb.grphx.setPaint(Color.GREEN);

//         Enemy enms[] = new Enemy[lvl.entities.size()];
//         for(int i = 0;i<lvl.entities.size();i++){
//         if(true){
//             enms[i] = (Enemy)i;
//         }
//         }
        
//         for(int i = 0;i<enms.length;i++){
//         System.out.println((enms[i].posY));
//         chb.grphx.fillRect((int)(enms[i].posX*32), (int)(enms[i].posY*32) + 50, (int)((enms[i].health/enms[i].maxHealth)*40), chb.hBarHeight);
//         }
        
        
// // chb.grphx.setPaint(Color.GREEN);
// //         chb.grphx.setStroke(new BasicStroke(1.0f));
// //         chb.grphx.fillRect(0, 0, (int)(h*10), mhb.hBarHeight);
// //         chb.grphx.setColor(Color.BLACK);
// //         chb.grphx.setPaint(Color.BLACK);
// //         chb.grphx.drawRect(0, 0, 99, mhb.hBarHeight-1);

//             }
//             catch (Exception e) {
//                 System.out.println("Stuff didn't work");
//                 System.out.println(e.getMessage());
//             }
//         }
//     }
// }


package towerGame;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.List;

import entity.Entity;
import entity.LivingEntity;
import main.Main;
import map.Level;

import java.util.ArrayList;

public class HealthBarManager {
    private float prevHealth = -1;
    private float prevMana = -1;

    public class MainHealthBarManager {
	    public float health = 5;
	    public float mana = 6f;
	    public float maxHealth = 10;
	    public int hBarWidth = 100;
	    public int mBarWidth = 100;
	    public int hBarHeight = 10;
	    public int mBarHeight = 10;
	    public float minimumHealthForChangedAppearanceOfHealthBar = 3;
	    BufferedImage img = new BufferedImage(hBarWidth,hBarHeight+mBarHeight, BufferedImage.TYPE_4BYTE_ABGR);
	    Graphics2D grphx = (Graphics2D)img.getGraphics();
    }

    public class CharacterHealthBarManager {
	    public float health = 3.1f;
	    public float mana = 15;
	    public float maxHealth = 10;
	    public int hBarWidth = 100;
	    public int hBarHeight = 3;
	    public int Width = 320*Main.scale;
	    public int Height = 240*Main.scale;
	    public final int framerulesperframe = 2;
	    private int count = 0;
	    public boolean isDrawing = false;
	    public float minimumHealthForChangedAppearanceOfHealthBar = 2;
	    //BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR);
	    Graphics2D grphx;// = (Graphics2D)img.getGraphics();
    }
    public MainHealthBarManager mhb;
    public CharacterHealthBarManager chb;
    public HealthBarManager(){
        mhb = new MainHealthBarManager();
        chb = new CharacterHealthBarManager();
        render(null, 5f, 6f, null);
        prevHealth = 10;
    }
    public void refreshBar() {
    	prevHealth=-1;
    	prevMana=-1;
    }

    public void render(Graphics2D c, float h, float m, Level lvl){
        if((prevHealth != h || prevMana != m) && c != null){
	        mhb.grphx = (Graphics2D)mhb.img.getGraphics();
	        mhb.grphx.setComposite(AlphaComposite.Clear);
	        mhb.grphx.fillRect(0, 0, mhb.hBarWidth, mhb.hBarHeight+mhb.mBarHeight);
	        mhb.grphx.setComposite(AlphaComposite.SrcOver);
	        if(h > mhb.minimumHealthForChangedAppearanceOfHealthBar){
		        mhb.grphx.setPaint(Color.GREEN);
		        mhb.grphx.setStroke(new BasicStroke(1.0f));
		        mhb.grphx.fillRect(0, 0, (int)(h*10), mhb.hBarHeight);
		        mhb.grphx.setColor(Color.BLACK);
		        mhb.grphx.setPaint(Color.BLACK);
		        mhb.grphx.drawRect(0, 0, 99, mhb.hBarHeight-1);
	        }else{
		        mhb.grphx.setPaint(Color.RED);
		        mhb.grphx.setStroke(new BasicStroke(1.0f));
		        mhb.grphx.fillRect(0, 0, (int)(h*10), mhb.hBarHeight);
		        mhb.grphx.setPaint(Color.BLACK);
		        
		        for(int i = 0;i<(h>=0.5?(h*2)-1:0);i++){
		            mhb.grphx.drawLine(i*5, mhb.hBarHeight, (i*5)+5, 0);
		        }
		        mhb.grphx.setColor(Color.BLACK);
		        mhb.grphx.setPaint(Color.BLACK);
		        mhb.grphx.drawRect(0, 0, 99, mhb.hBarHeight-1);
		        mhb.grphx.setColor(Color.WHITE);
		        mhb.grphx.setPaint(Color.WHITE);
	        }
	
	
	        mhb.grphx.setFont(new Font("Serif", Font.PLAIN, 12));
	        mhb.grphx.drawString(""+(Math.floor(h*10.0)/10), h >= 2.5 ? ((int)(h*10)-22) : 3, 9);
	        
	        mhb.grphx.setPaint(Color.BLUE);
	        mhb.grphx.setStroke(new BasicStroke(1.0f));
	        mhb.grphx.fillRect(0, 10, (int)((m/15)*100), mhb.mBarHeight);
	        mhb.grphx.setColor(Color.BLACK);
	        mhb.grphx.setPaint(Color.BLACK);
	        mhb.grphx.drawRect(0, 10, 99, mhb.mBarHeight-1);
	        
	
	        mhb.grphx.setColor(Color.WHITE);
	        mhb.grphx.setPaint(Color.WHITE);
	        mhb.grphx.setFont(new Font("Serif", Font.PLAIN, 12));
	        mhb.grphx.drawString(String.valueOf(Math.round(m*10.0D)/10.0F), m >= 4.1 ? ((int)((m/15)*100))-22 : 3, 9+10);
        }
        if(c != null){
	        c.drawImage(mhb.img, 0, 0, null);
	        renderSprites(c, h, m, lvl);
        }
        
        prevHealth = h;
        prevMana = m;
        chb.count++;
        mhb.grphx.dispose();
    }

    public void renderSprites(Graphics2D c, float h, float m, Level lvl){
    	// Clear the screen instead of declaring a new one. This saves a lot of memory.
        //chb.img = new BufferedImage(chb.Width, chb.Height, BufferedImage.TYPE_4BYTE_ABGR);
    	// or just render it on the already existing screen
        chb.grphx = c;//(Graphics2D)chb.img.getGraphics();
        //chb.grphx.setBackground(new Color(255, 255, 255, 0));
        //chb.grphx.clearRect(0,0, (int)chb.img.getWidth(), (int)chb.img.getHeight());
        int j=0;
        for(Entity i : lvl.entities){
	        if(/*!i.hbishidden && */i instanceof LivingEntity){
		        
			    chb.grphx.setColor(Color.GREEN);
			    chb.grphx.setPaint(((LivingEntity)i).health < 3 ? Color.RED : Color.GREEN);  
			    int[] positions = i.getPositionOnScreen();
			    try{
			    	chb.grphx.fillRect((positions[0])-(((int)((((LivingEntity)i).health/((LivingEntity)i).maxHealth)*(20*Main.scale))-(Main.tileSize))/2), Math.abs(positions[1] - 20)==(positions[1] - 20) ? (positions[1] - 20) : (positions[1] + 20), (int)((((LivingEntity)i).health/((LivingEntity)i).maxHealth)*(20*Main.scale)), chb.hBarHeight*Main.scale);
			    } catch(Exception e){
			        e.printStackTrace();
			    }
		        
	        }
	        j++;
        }
        Player i=lvl.player;
        mhb.health = i.health;
        
        
    }
}
