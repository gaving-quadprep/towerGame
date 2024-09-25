package util;

public enum Direction {
	LEFT,RIGHT,UP,DOWN;
	public double toNumber() {
		switch(this) {
		case LEFT:
		case UP:
			return -1.0;
		case RIGHT:
		case DOWN:
			return 1.0;
		}
		return 0.0;
	}
}