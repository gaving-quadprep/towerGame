package entity;

import java.util.HashMap;
import java.util.List;

public final class EntityRegistry {
	public static final HashMap<String, Class<Entity>> entities = new HashMap<String, Class<Entity>>();
	public static final class RegisteredEntity<T> {
		public Class<T> entityClass;
		public List<T> entityVariants;
		public T entityDefault;
		public String name;
		public int id;
	}
}
