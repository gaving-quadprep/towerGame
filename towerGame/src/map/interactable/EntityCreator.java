package map.interactable;

import java.awt.Rectangle;

public class EntityCreator extends TileWithData {


	public EntityCreator(int textureId, boolean isSolid) {
		super(textureId, isSolid);
		// TODO Auto-generated constructor stub
	}

	protected EntityCreator(int textureId, boolean isSolid, Rectangle hitbox) {
		super(textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
	
	public EntityCreator(int id, int textureId, boolean isSolid) {
		super(id, textureId, isSolid);
		// TODO Auto-generated constructor stub
	}
	
	public EntityCreator(int id, int textureId, boolean isSolid, Rectangle hitbox) {
		super(id, textureId, isSolid, hitbox);
		// TODO Auto-generated constructor stub
	}
}
