package main;

import java.util.HashMap;

public final class Registry<T> {
	public HashMap<String, Class<? extends T>> map = new HashMap<String, Class<? extends T>>();
	public HashMap<Class<? extends T>, String> mapReverse = new HashMap<Class<? extends T>, String>();
	public void addMapping(Class<? extends T> clazz, String name) {
		map.put(name, clazz);
		mapReverse.put(clazz, name);
	}
	public T createByName(String name, @SuppressWarnings("rawtypes") Class[] paramc, Object[] param) {
		T t = null;

		try {
			Class<? extends T> clazz = map.get(name);
			if(clazz != null) {
				t = (T)clazz.getConstructor(paramc).newInstance(param);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return t;
	}
	public String getClassName(Class<? extends T> clazz) {
		return mapReverse.get(clazz);
	}
}
