package levelEditor;

public enum Tool {
	DRAWTILES,
	FILLTILES,
	ADDENTITY,
	MOVEENTITY,
	REMOVEENTITY,
	PLACEDECORATION;
	public static Tool fromNumber(int n) {
		switch(n) {
		case 0:
			return DRAWTILES;
		case 1:
			return FILLTILES;
		case 2:
			return ADDENTITY;
		case 3:
			return MOVEENTITY;
		case 4:
			return REMOVEENTITY;
		default:
			return null;
		}
	}
}
