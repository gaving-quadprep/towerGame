package map.interactable;

import save.SerializedData;

public abstract class TileData {
	public abstract SerializedData serialize();
	public abstract void deserialze(SerializedData sd);
}
