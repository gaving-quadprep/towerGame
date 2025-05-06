package util;

import java.util.Collection;
import java.util.HashMap;

public class Registry<T> {
	private HashMap<String, T> map = new HashMap<String, T>();
	private HashMap<T, String> mapReverse = new HashMap<T, String>();
	
	public void addMapping(T t, String name) {
		map.put(name, t);
		mapReverse.put(t, name);
	}
	public void addMappingLegacy(T t, String name) {
		map.put(name, t);
	}
	public T get(String name) {
		return map.get(name);
	}
	public String getName(T t) {
		return mapReverse.get(t);
	}
	public Collection<String> getNames() {
		return mapReverse.values(); // do this instead of map.keySet to ignore legacy mappings
	}
	public Collection<T> getValues() {
		return mapReverse.keySet();
	}
}
