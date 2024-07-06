package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import game.GameModel;
import game.GameView;
import game.Inventory;
import items.Item;
import living.Player;
import utility.Generators;
import utility.Point;
import utility.Rectangle;

public class Shop implements BuyingRoom {
	private final Color color;
	private final String imageID;
	private boolean isVisited = false;
	private final List<Item> content;
	private final HashMap<Rectangle, Integer> contentCoordinates;
	// Key = Item index ; Value = Price
	private final HashMap<Integer, Integer> prices;

	public Shop(List<Item> content, String imageID, Color color) {
		this.content = new ArrayList<>(List.copyOf(content));
		this.contentCoordinates = new HashMap<>();
		this.imageID = imageID;
		this.color = color;
		this.prices = new HashMap<>();
		givePrices(this.content);
	}

	public Shop(Item... content) {
		this(List.of(content), "shop.png", new Color(118, 65, 52));
	}

	public Shop(List<Item> content) {
		this(content, "shop.png", new Color(118, 65, 52));
	}

	@Override
	public String id() {
		return imageID;
	}

	public void givePrices(List<Item> content) {
		for (Item item : content) {
			givePrice(item);
		}
	}

	public void givePrice(Item item) {
		prices.put(item.id(), Generators.randint(5, 9) * (item.rarity() + 1));
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

	public void addContentCoordinates(Rectangle imageProperties, Integer i) {
		contentCoordinates.put(imageProperties, i);
	}

	@Override
	public boolean isVisited() {
		return isVisited;
	}

	public int price(int id) {
		return prices.get(id);
	}


	public void justVisited() {
		isVisited = content.isEmpty();
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
		data.treasureChest(player,this);
	}

	@Override
	public void drawRoom(Graphics2D graphics, GameView view) {
		view.drawBuyingRoom(graphics, this);
	}

	public static Shop generateShop() {
		 List<Item> newShop = new ArrayList<>();
		 for (Item item : (GameModel.getRandomItems(0,4))) {
			 item.setPrice();
			 newShop.add(item);
		 }
		 return new Shop(newShop);
	}

	@Override
	public boolean enoughRessources(Inventory inventory, Item item) {
		return inventory.enoughRessources(item.price());
	}
}
