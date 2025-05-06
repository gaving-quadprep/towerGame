package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.Main;
import main.WorldRenderer;
import map.Level;
import save.ISerializable;
import save.SerializedData;
import util.ClassRegistry;
import util.Direction;
import util.Position;
import java.awt.Rectangle;

public abstract class Entity implements ISerializable, Cloneable {
	public static final ClassRegistry<Entity> entityRegistry = new ClassRegistry<Entity>();
	public BufferedImage sprite;
	public boolean customSprite = false;
	public double x;
	public double y;
	public long id;
	public Rectangle hitbox;
	public transient Level level;
	public boolean markedForRemoval;
	public boolean canBeStoodOn = false;
	public Entity(Level level) {
		this.level = level;
	}
	public String toString() {
		String className = this.getClass().getSimpleName();
		if(className.equals("")) {
			className = "? extends " + this.getClass().getSuperclass().getSimpleName();
		}
		return String.format("%s (%.2f,%.2f)", className, this.x, this.y);
	}
	public Object clone() { 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Entity e2 = entityRegistry.createByName(entityRegistry.getClassName(this.getClass()), new Class[] {Level.class}, new Object[] {level});
			e2.deserialize(this.serialize());
			return e2;
		} 
	}
	public void update() {}
	public void render(WorldRenderer wr) {
		wr.drawImage(this.sprite, this.x, this.y, 1, 1);
	}
	public void renderDebug(Graphics2D g2) {}
	
	public String getSprite() { 
		return null;
	}
	public int getSpriteWidth() {
		return 16;
	}
	public void loadSprites() {
		String spriteName = this.getSprite();
		if (spriteName != null)
			this.sprite = level.getSprite(spriteName);
	}
	
	public void setSprite(BufferedImage sprite) {this.sprite=sprite;}
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setPosition(Position p) {
		this.x = p.x;
		this.y = p.y;
	}
	public final int[] getPositionOnScreen() {
		int[] positions = {(int)Math.round(this.x*Main.tileSize-this.level.cameraX*Main.tileSize),(int)Math.round(this.y*Main.tileSize-(int)(level.cameraY*Main.tileSize))};
		return positions;
	}

	public void move(double motion, Direction direction) {
		switch(direction) {
		case UP:
			this.setPosition(this.x, this.y - motion);
			break;
		case DOWN:
			this.setPosition(this.x, this.y + motion);
			break;
		case LEFT:
			this.setPosition(this.x - motion, this.y);
			break;
		case RIGHT:
			this.setPosition(this.x + motion, this.y);
			break;
		}
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(entityRegistry.getClassName(this.getClass()), "class");
		sd.setObject(this.x, "x");
		sd.setObject(this.y, "y");
		sd.setObject(this.id, "id");
		sd.setObject(this.hitbox, "hitbox");
		sd.setObject(this.customSprite, "customSprite");
		if(this.customSprite) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				ImageIO.write(this.sprite, "png", stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sd.setObject(stream.toByteArray(), "sprite");
		}
		sd.setObject(this.canBeStoodOn, "canBeStoodOn");
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		this.x = (double)sd.getObjectDefault("x",0);
		this.y = (double)sd.getObjectDefault("y",0);
		this.id = (long)sd.getObjectDefault("id",-1);
		this.hitbox = (Rectangle)sd.getObjectDefault("hitbox", new Rectangle(0,0,0,0));
		this.customSprite = (boolean)sd.getObjectDefault("customSprite", false);
		if(this.customSprite) {
			ByteArrayInputStream stream = new ByteArrayInputStream((byte[])sd.getObjectDefault("sprite",null));
			if(stream!=null) {
				try {
					this.sprite = ImageIO.read(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.canBeStoodOn = (boolean)sd.getObjectDefault("canBeStoodOn", false);
	}
	public static JPanel getCustomOptions() {
		return null;
	}
	public String getDebugString() {
		return "";
	}
	static {
		entityRegistry.addMapping(Decoration.class, "Decoration");
		entityRegistry.addMapping(LivingEntity.class, "LivingEntity");
		entityRegistry.addMapping(Enemy.class, "Enemy");
		entityRegistry.addMapping(FireEnemy.class, "FireEnemy");
		entityRegistry.addMapping(Thing.class, "Thing");
		entityRegistry.addMapping(NPC.class, "NPC");
		entityRegistry.addMapping(FireProjectile.class, "FireProjectile");
		entityRegistry.addMapping(PlayerProjectile.class, "PlayerProjectile");
		entityRegistry.addMapping(FallingTile.class, "FallingTile");
		entityRegistry.addMapping(ManaOrb.class, "ManaOrb");
		entityRegistry.addMapping(FloatingPlatform.class, "FloatingPlatform");
		entityRegistry.addMapping(FlameDemon.class, "FlameDemon");
		entityRegistry.addMapping(PuddleMonster.class, "PuddleMonster");
		entityRegistry.addMapping(ZombieKnight.class, "ZombieKnight");
		entityRegistry.addMapping(BlazingShadow.class, "BlazingShadow");
		entityRegistry.addMapping(RageSpawn.class, "RageSpawn");
		entityRegistry.addMapping(Sheep.class, "Sheep");
		entityRegistry.addMapping(Explosion.class, "Explosion");
		entityRegistry.addMapping(Bomb.class, "Bomb");
		entityRegistry.addMapping(DroppedItem.class, "DroppedItem");
	}
}
