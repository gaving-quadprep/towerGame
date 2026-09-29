package util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Registry<T> {
	private final Map<String, T> map;
	private final Map<T, String> mapReverse;

	public Registry(boolean ordered) {
		if (ordered) {
			map = new LinkedHashMap<>();
			mapReverse  = new LinkedHashMap<>();
		} else {
			map = new HashMap<>();
			mapReverse  = new HashMap<>();
		}
	}

	public Registry() {
		this(false);
	}

	public void addMapping(T t, String name) {
		map.put(name, t);
		mapReverse.put(t, name);
	}
	public T get(String name) {
		return map.get(name);
	}
	public String getName(T t) {
		return mapReverse.get(t);
	}
	public Collection<String> getNames() {
		return map.keySet();
	}
	public Collection<T> getValues() {
		return map.values();
	}
	public Collection<Map.Entry<String, T>> getPairs() {
		return map.entrySet();
	}
}
