package save;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;

public class GameSerializable implements Serializable {
	private static final long serialVersionUID = -1014306180158796118L;
	List<Entity> entities = new ArrayList<Entity>();
	float playerX;
	float playerY;
	float playerStartX;
	float playerStartY;
	float playerHealth;
	float playerMana;
	float playerArmor;
	int playerWeapon;
	int mapTilesForeground[][];
	int mapTilesBackground[][];
	int levelSizeX;
	int levelSizeY;
	Color skyColor;
}
