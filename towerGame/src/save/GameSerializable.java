package save;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class GameSerializable implements Serializable {
	private static final long serialVersionUID = -1014306180158796118L;
	List<SerializedData> entities = new ArrayList<SerializedData>();
	SerializedData attr = new SerializedData();
}
