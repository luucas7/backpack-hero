package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import game.GameModel;
import game.GameView;
import items.Item;
import living.Player;
import utility.Generators;
import utility.Point;
import utility.Rectangle;

public class Treasure implements ContentRoom {
	private final Color color;
	private final String imageID;
	private boolean isVisited = false;
	private final List<Item> content;
	private final HashMap<Rectangle, Integer> contentCoordinates;

	public Treasure(List<Item> content, String imageID, Color color) {
		this.content = new ArrayList<>(List.copyOf(content));
		this.contentCoordinates = new HashMap<>();
		this.imageID = imageID;
		this.color = color;
	}

	public Treasure(Item... content) {
		this(List.of(content), "treasure.png", new Color(118, 65, 52));
	}

	public Treasure(List<Item> content) {
		this(content, "treasure.png", new Color(118, 65, 52));
	}

	@Override
	public String id() {
		return imageID;
	}

	@Override
	public Color color() {
		return color;
	}

	public List<Item> content() {
		return content;
	}

	public HashMap<Rectangle, Integer> contentCoordinates() {
		return contentCoordinates;
	}
	// Map the coordinates on screen to the index i in the content list
	public void addContentCoordinates(Rectangle imageCoordinates, Integer i) {
		contentCoordinates.put(imageCoordinates, i);
	}

	@Override
	public boolean isVisited() {
		return isVisited;
	}

	public void justVisited() {
		isVisited = true;//content.isEmpty();
		content().clear();
	}

	public boolean removeWithValue(int i) {

		for (var couple : contentCoordinates.entrySet()) {
			if (couple.getValue() == i) {
				contentCoordinates.remove(couple.getKey());
				return true;
			}
		}

		return false;
	}

	@Override
	public void doAction(GameModel data, Player player, Point xy) {
		data.treasureChest(player, this);
	}

	@Override
	public void drawRoom(Graphics2D graphics, GameView view) {
		view.drawTreasure(graphics, this);
	}

	public static Treasure generateTreasure(Item... items) {
		int chestSize = Generators.randint(2, 3);
		return new Treasure(GameModel.getRandomItems(0, chestSize, items));
	}

	public static Treasure generateTreasure(String imageID, Color color, Item... items) {
		int chestSize = Generators.randint(2, 3);
		return new Treasure(GameModel.getRandomItems(0, chestSize, items), imageID, color);
	}

}
