package save;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;

public class GameSerializable implements Serializable {
	private static final long serialVersionUID = -1014306180158796118L;
	List<Entity> entities = new ArrayList<Entity>();
	double playerX;
	double playerY;
	double playerStartX;
	double playerStartY;
	double playerHealth;
	double playerMana;
	double playerArmor;
	int playerWeapon;
	int mapTilesForeground[][];
	int mapTilesBackground[][];
	int levelSizeX;
	int levelSizeY;
	Color skyColor;
}
