package util;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// no, i am not being paid based on how many generics i use
public class SuperClassFinder<T> {
	private final Class<T> endClass;
	public SuperClassFinder(Class<T> endClass) {
		this.endClass = endClass;
	}
	
	private Map<Class<? extends T>, List<Class<? extends T>>> classCache = new HashMap<Class<? extends T>, List<Class<? extends T>>>();
	
	public <C extends T> List<Class<? extends T>> getSuperclasses(Class<C> clazz) {
		List<Class<? extends T>> list = classCache.get(clazz); // for some reason only this works and not (List<Class<? extends T>>)
		if (list == null) {
			list = new ArrayList<Class<? extends T>>();
			Class<? extends T> superclass = clazz;
			while(superclass != endClass) {
				list.add(superclass);
				superclass = (Class<? extends T>) superclass.getSuperclass();
			}
			classCache.put(clazz, list);
		}
		return list;
	}
}
