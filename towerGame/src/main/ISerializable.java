package main;

import save.SerializedData;

public interface ISerializable {
	public SerializedData serialize();
	public void deserialize(SerializedData sd);
}
