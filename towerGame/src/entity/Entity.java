package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.enemy.BlazingShadow;
import entity.enemy.BombGuy;
import entity.enemy.Enemy;
import entity.enemy.FireEnemy;
import entity.enemy.FireProjectile;
import entity.enemy.FlameDemon;
import entity.enemy.PuddleMonster;
import entity.enemy.RageSpawn;
import entity.enemy.Sentinel;
import entity.enemy.Thing;
import entity.enemy.ZombieKnight;
import main.WorldRenderer;
import map.Level;
import save.ISerializable;
import save.SerializedData;
import util.ClassRegistry;
import util.Direction;
import util.Position;
import java.awt.Rectangle;

public abstract class Entity implements ISerializable, Cloneable {
	public static final ClassRegistry<Entity> entityRegistry = new ClassRegistry<>();
	public BufferedImage sprite;
	public boolean customSprite = false;
	public double x;
	public double y;
	public long id;
	public Rectangle hitbox;
	public transient Level level;
	public boolean markedForRemoval;
	public Entity(Level level) {
		this.level = level;
	}
	@Override
	public String toString() {
		String className = this.getClass().getSimpleName();
		if(className.equals("")) {
			className = "? extends " + this.getClass().getSuperclass().getSimpleName();
		}
		return String.format("%s (%.2f,%.2f)", className, x, y);
	}
	@Override
	public Object clone() { 
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Entity e2 = entityRegistry.createByName(entityRegistry.getClassName(this.getClass()), new Class[] {Level.class}, new Object[] {level});
			e2.deserialize(serialize());
			return e2;
		} 
	}
	public void update() {}
	public void render(WorldRenderer wr) {
		wr.drawImage(sprite, x, y, 1, 1);
	}
	public void renderDebug(Graphics2D g2) {}

	public String getSprite() { 
		return null;
	}
	public int getSpriteWidth() {
		return 16;
	}
	public void loadSprites() {
		String spriteName = getSprite();
		if (spriteName != null)
			sprite = level.getSprite(spriteName);
	}

	public void setSprite(BufferedImage sprite) {this.sprite=sprite;}
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setPosition(Position p) {
		x = p.x;
		y = p.y;
	}

	public void doDamageTo(LivingEntity le, double damage) {
		le.damage(damage, new EntityDamageSource(this));
	}

	public void move(double motion, Direction direction) {
		switch(direction) {
		case UP:
			this.setPosition(x, y - motion);
			break;
		case DOWN:
			this.setPosition(x, y + motion);
			break;
		case LEFT:
			this.setPosition(x - motion, y);
			break;
		case RIGHT:
			this.setPosition(x + motion, y);
			break;
		}
	}
	@Override
	public SerializedData serialize() {
		SerializedData sd = new SerializedData();
		sd.setObject(entityRegistry.getClassName(this.getClass()), "class");
		sd.setObject(x, "x");
		sd.setObject(y, "y");
		sd.setObject(id, "id");
		sd.setObject(hitbox, "hitbox");
		sd.setObject(customSprite, "customSprite");
		if(customSprite) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			try {
				ImageIO.write(sprite, "png", stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sd.setObject(stream.toByteArray(), "sprite");
		}
		return sd;
	}
	@Override
	public void deserialize(SerializedData sd) {
		x = (double)sd.getObjectDefault("x",0);
		y = (double)sd.getObjectDefault("y",0);
		id = (long)sd.getObjectDefault("id",-1);
		hitbox = (Rectangle)sd.getObjectDefault("hitbox", new Rectangle(0,0,0,0));
		customSprite = (boolean)sd.getObjectDefault("customSprite", false);
		if(customSprite) {
			ByteArrayInputStream stream = new ByteArrayInputStream((byte[])sd.getObjectDefault("sprite",null));
			if(stream!=null) {
				try {
					sprite = ImageIO.read(stream);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
		entityRegistry.addMapping(FallingPlatform.class, "FallingPlatform");
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
		entityRegistry.addMapping(Sentinel.class, "Sentinel");
		entityRegistry.addMapping(TrackingPlayerProjectile.class, "TrackingPlayerProjectile");
		entityRegistry.addMapping(BombGuy.class, "BombGuy");
	}
}
