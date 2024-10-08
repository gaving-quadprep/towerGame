package levelEditor;

import util.Position;

public enum Tool {
	DRAWTILES {
		@Override public void onMouseRightClick(Position p) {
			
		}
	},
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
			return ADDENTITY;
		case 2:
			return MOVEENTITY;
		case 3:
			return REMOVEENTITY;
		case 4:
			return FILLTILES;
		case 5:
			return PLACEDECORATION;
		default:
			return null;
		}
	}
	public void onMouseLeftClick(Position p) {}
	public void onMouseRightClick(Position p) {}
}
