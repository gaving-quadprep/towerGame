package map;

public class FlowerTile extends Tile {
	byte type;
	public FlowerTile(byte id, byte type) {
		super(id,type+16,false);
	}
	/*public int getTextureId() {
		return this.type+16;
	}*/
}
