package util;

public final class ClassRegistry<T> extends Registry<Class<? extends T>> {
	public T createByName(String name, @SuppressWarnings("rawtypes") Class[] paramc, Object[] param) {
		T t = null;

		try {
			Class<? extends T> clazz = this.get(name);
			if(clazz != null) {
				t = (T)clazz.getConstructor(paramc).newInstance(param);
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return t;
	}
	
	public String getClassName(Class<? extends T> clazz) {
		return this.getName(clazz);
	}
}
