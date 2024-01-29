package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import entity.*;

import java.util.Objects;

public class EntityRegistry {
	private static final HashMap<String, Class> entityTypes = new HashMap<String, Class>();
	public static void registerClass(Class eclass) {
		entityTypes.putIfAbsent(eclass.getName().replaceFirst("towerGame.entity.", ""), eclass);
	}
	public static Class getClass(String estring) {
		return entityTypes.get(estring);
	}
	public static String getRegistryFromClass(Entity e) {
		return getKeyByValue(entityTypes, e.getClass());
	}
				
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}
	static {
		registerClass(Entity.class);
		registerClass(LivingEntity.class);
		registerClass(Enemy.class);
		registerClass(FireEnemy.class);
		registerClass(Thing.class);
		registerClass(NPC.class);
		registerClass(ManaOrb.class);
		registerClass(FireProjectile.class);
		registerClass(PlayerProjectile.class);
		registerClass(FallingBoulder.class);
	}
}
