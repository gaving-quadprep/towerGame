package save;

public interface ISerializable {
	public SerializedData serialize();
	public void deserialize(SerializedData sd);
}
