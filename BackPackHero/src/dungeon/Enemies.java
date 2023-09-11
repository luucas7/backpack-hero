package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import game.GameModel;
import game.GameView;
import items.Gold;
import items.Key;
import living.Enemy;
import living.Player;
import utility.Generators;
import utility.Point;
import utility.Rectangle;

public class Enemies implements ContentRoom {
	private final Color color = new Color(191, 33, 30);
	private static final String imageID = "enemies.png";
	private final List<Enemy> content;
	private boolean hasAKey = false;
	private boolean isVisited = false;
	private final HashMap<Rectangle, Integer> contentCoordinates;
	private static final List<Enemy> gameEnemies = new ArrayList<>();
	

	private int target = 0;

	public Enemies(List<Enemy> enemies) {
		this.content = new ArrayList<>(enemies);
		this.contentCoordinates = new HashMap<>();
	}

	public static void addEnemy(Enemy... enemy) {
		gameEnemies.addAll(List.of(enemy));
	}

	@Override
	public String id() {
		return imageID;
	}

	@Override
	public Color color() {
		return color;
	}

	public int target() {
		return target;
	}

	@Override
	public boolean isVisited() {
		return isVisited;
	}

	public void justVisited() {
		isVisited = true;
	}

	@Override
	public boolean isBlocking() {
		return !content.isEmpty();
	}
	

	public Treasure setTreasure() {
		return hasAKey ? Treasure.generateTreasure(imageID, color,Gold.create(),Key.create())
				       : Treasure.generateTreasure(imageID, color,Gold.create());
	}

	@Override
	public HashMap<Rectangle, Integer> contentCoordinates() {
		return contentCoordinates;
	}

	@Override
	public List<Enemy> content() {
		return content;
	}

	public void setTarget(int i) {
		target = i;
	}

	public void addContentCoordinates(Rectangle imageProperties, Integer i) {
		contentCoordinates.put(imageProperties, i);
	}

	@Override
	public void doAction(GameModel data, Player player, Point xy) {
		data.enemies(player, xy, this);
	}

	@Override
	public boolean hasItemClickingPriority() {
		return true;
	}

	@Override
	public void drawRoom(Graphics2D graphics, GameView view) {
		view.drawEnemies(graphics, this);
	}

	public static Enemies generateEnemies() {
		int enemiesSize = Generators.randint(2, 3);
		List<Enemy> newEnemies = new ArrayList<>();
		
		for (int i = 0; i < enemiesSize; i++) {

			newEnemies.add((gameEnemies.get(Generators.randint(gameEnemies.size() - 1))).copy());
		}
		return new Enemies(newEnemies);
	}

	public void gotAKey() {
		this.hasAKey = true;
	}

}
