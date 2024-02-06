package entity;

import java.util.HashMap;

import map.Level;

public final class EntityRegistry {
	public static HashMap<String, Class<? extends Entity>> entities = new HashMap<String, Class<? extends Entity>>();
	public static HashMap<Class<? extends Entity>, String> entitiesReverse = new HashMap<Class<? extends Entity>, String>();
	public static void addMapping(Class<? extends Entity> clazz, String name) {
		entities.put(name, clazz);
		entitiesReverse.put(clazz, name);
	}
	public static Entity createEntityByName(String name, Level level) {
		Entity e = null;

		try {
			Class<? extends Entity> clazz = entities.get(name);
			if(clazz != null) {
				e = (Entity)clazz.getConstructor(new Class[]{Level.class}).newInstance(new Object[]{level});
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return e;
	}
	public static String getClassName(Class<? extends Entity> clazz) {
		return entitiesReverse.get(clazz);
	}
	static {
		addMapping(Decoration.class, "Decoration");
		addMapping(LivingEntity.class, "LivingEntity");
		addMapping(Enemy.class, "Enemy");
		addMapping(FireEnemy.class, "FireEnemy");
		addMapping(Thing.class, "Thing");
		addMapping(NPC.class, "NPC");
		addMapping(FireProjectile.class, "FireProjectile");
		addMapping(PlayerProjectile.class, "PlayerProjectile");
		addMapping(FallingBoulder.class, "FallingBoulder");
		addMapping(ManaOrb.class, "ManaOrb");
	}
}
