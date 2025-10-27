package entity;

public abstract class DamageSource<T> {
	private T source;
	public DamageSource(T source) {
		this.source = source;
	}
	public T getSource() {
		return source;
	}
 }
