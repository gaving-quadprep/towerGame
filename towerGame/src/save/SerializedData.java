package save;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class SerializedData implements Serializable {
	private static final long serialVersionUID = 1634188708210409712L;
	public static enum SaveableClasses {
		BYTE,
		INT,
		LONG,
		BOOLEAN,
		FLOAT,
		DOUBLE,
		STRING,
		LIST,
		BYTEARRAY,
		INTARRAY,
		INTARRAY2D,
		INTARRAY3D,
		RECTANGLE,
		COLOR,
		SERIALIZEDDATA,
		VALUEARRAY
	}
	public static class Value implements Serializable {
		private static final long serialVersionUID = 4941868005060419656L;
		public SaveableClasses type;
		public Object val;
		public Value(SaveableClasses type, Object val) {
			this.type=type;
			this.val=val;
		}
	}
	public HashMap<String, Value> savedData;
	public SerializedData() {
		this.savedData = new HashMap<String, Value>();
	}
	public void setObject(byte obj, String name) {
		savedData.put(name, new Value(SaveableClasses.BYTE, obj));
	}
	public void setObject(int obj, String name) {
		savedData.put(name, new Value(SaveableClasses.INT, obj));
	}
	public void setObject(long obj, String name) {
		savedData.put(name, new Value(SaveableClasses.LONG, obj));
	}
	public void setObject(boolean obj, String name) {
		savedData.put(name, new Value(SaveableClasses.BOOLEAN, obj));
	}
	public void setObject(float obj, String name) {
		savedData.put(name, new Value(SaveableClasses.FLOAT, obj));
	}
	public void setObject(double obj, String name) {
		savedData.put(name, new Value(SaveableClasses.DOUBLE, obj));
	}
	public void setObject(String obj, String name) {
		savedData.put(name, new Value(SaveableClasses.STRING, obj));
	}
	public void setObject(List<?> obj, String name) {
		savedData.put(name, new Value(SaveableClasses.LIST, obj));
	}
	public void setObject(byte[] obj, String name) {
		savedData.put(name, new Value(SaveableClasses.BYTEARRAY, obj));
	}
	public void setObject(int[] obj, String name) {
		savedData.put(name, new Value(SaveableClasses.INTARRAY, obj));
	}
	public void setObject(int[][] obj, String name) {
		savedData.put(name, new Value(SaveableClasses.INTARRAY2D, obj));
	}
	public void setObject(int[][][] obj, String name) {
		savedData.put(name, new Value(SaveableClasses.INTARRAY3D, obj));
	}
	public void setObject(Rectangle obj, String name) {
		savedData.put(name, new Value(SaveableClasses.RECTANGLE, obj));
	}
	public void setObject(Color obj, String name) {
		savedData.put(name, new Value(SaveableClasses.COLOR, obj));
	}
	public void setObject(SerializedData obj, String name) {
		savedData.put(name, new Value(SaveableClasses.SERIALIZEDDATA, obj));
	}
	public void setObject(Value[] obj, String name) {
		savedData.put(name, new Value(SaveableClasses.VALUEARRAY, obj));
	}
	
	protected Object getObject(String name) {
		Value v = savedData.get(name);
		if(v == null) {
			return null;
		}
		switch(v.type) {
		case BOOLEAN:
			return (boolean)v.val;
		case BYTE:
			return (byte)v.val;
		case BYTEARRAY:
			return (byte[])v.val;
		case COLOR:
			return (Color)v.val;
		case DOUBLE:
			return (double)v.val;
		case FLOAT:
			return (float)v.val;
		case INT:
			return (int)v.val;
		case INTARRAY:
			return (int[])v.val;
		case INTARRAY2D:
			return (int[][])v.val;
		case INTARRAY3D:
			return (int[][][])v.val;
		case LIST:
			return (List<?>)v.val;
		case LONG:
			return (long)v.val;
		case RECTANGLE:
			return (Rectangle)v.val;
		case SERIALIZEDDATA:
			return (SerializedData)v.val;
		case STRING:
			return (String)v.val;
		case VALUEARRAY:
			return (Value[])v.val;
		default:
			return null;
		}
	}
	public Object getObjectDefault(String name, Object def) {
		Object obj = getObject(name);
		return obj == null ? def : obj;
	}
}
