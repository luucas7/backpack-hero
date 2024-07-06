package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import dungeon.*;
import items.Item;
import items.Quantifiable;
import items.Shield;
import items.Weapon;
import living.Enemy;
import living.Player;
import utility.Generators;
import utility.Point;
import utility.Rectangle;

public class GameModel {
	private static final HashMap<Integer, List<Item>> gameItems = new HashMap<>();
	private HashMap<Rectangle, Point> floorCellsToGrid;
	private static final HashSet<Integer> alreadyGeneratedItems = new HashSet<>();

	private int currentItemIDSelected;
	private Item currentItemSelected = null;
	private boolean isCurrentItemInventory = false;
	private final Inventory inventory;

	private Room currentRoom;

	private final int width = GameController.width();
	private final int height = GameController.height();

	public GameModel() {
		this.inventory = new Inventory();
		this.floorCellsToGrid = new HashMap<>();
	}

	public Room currentRoom() {
		return currentRoom;
	}

	public void startGame(Player player, int itemSize, int marginI, int marginY, Item... items) {
		setCurrentRoom(player.currentFloor().getFirstRoom());
		setfloorCellsToGrid(initiateFloorScreen(player.currentFloor()));

		inventory.startGame(itemSize, marginI, marginY, items);

	}

	public static void setItemsData(List<Item> allItems) {

		for (Item item : allItems) {
			if (!gameItems.containsKey(item.rarity())) {
				List<Item> itemList = new ArrayList<>();
				gameItems.put(item.rarity(), itemList);
			}
			gameItems.get(item.rarity()).add(item);
		}

	}

	public static List<Item> getRandomItems(int minRarity, int count, Item... items) {
		List<Item> generatedItems = new ArrayList<>();
		int i = 0;

		for (Item item : items) {
			if (Generators.nextBool(1, 2) || !item.isQuantifiable() && !item.isUsableInFight()) {
				generatedItems.add(item);
				i++;
			}
		}
		while (i < count) {

			List<Item> itemsOfThatRarity;
			int percent = Generators.randint(1, 100);

			if (percent <= 65) {
				itemsOfThatRarity = gameItems.get(0 + minRarity);
			} else if (percent <= 85) {
				itemsOfThatRarity = gameItems.get(1 + minRarity);
			} else if (percent <= 95) {
				itemsOfThatRarity = gameItems.get(Math.min(2 + minRarity, gameItems.size() - 1));
			} else {
				itemsOfThatRarity = gameItems.get(Math.min(3 + minRarity, gameItems.size() - 1));
			}

			Item itemChosen = itemsOfThatRarity.get(Generators.randint(itemsOfThatRarity.size() - 1));

			if (!generatedItems.contains(itemChosen)) {
				if (alreadyGeneratedItems.contains(itemChosen.id()) && (Generators.nextBool(3, 7))) {
					continue;
				}
				generatedItems.add(itemChosen.copy());
				alreadyGeneratedItems.add(itemChosen.id());
				i++;

			}
		}
		return generatedItems;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public void updateCurrentRoom(Player player) {
		setCurrentRoom(player.getRoom(player.i(), player.j()));
	}

	public void setCurrentItemSelected(Item item) {
		System.out.println("setCurrentItemSelected : " + item);
		this.currentItemSelected = item;
		if (currentItemSelected != null) {
			this.currentItemIDSelected = item.id();
		}
	}

	public Item currentItemSelected() {
		return currentItemSelected;
	}

	public int currentItemIDSelected() {
		return currentItemIDSelected;
	}

	public HashMap<Rectangle, Point> floorCellsToGrid() {
		return floorCellsToGrid;
	}

	public void setfloorCellsToGrid(HashMap<Rectangle, Point> map) {
		this.floorCellsToGrid = map;
	}

	public HashMap<Rectangle, Point> initiateFloorScreen(Floor currentFloor) {
		Objects.requireNonNull(currentFloor);
		var floorGrid = currentFloor.floorGrid();
		var result = new HashMap<Rectangle, Point>();

		for (int i = 0; i < currentFloor.lines(); i++) {
			for (int j = 0; j < currentFloor.columns(); j++) {
				if (floorGrid[i][j] != null) {
					result.put(new Rectangle(new Point(width / 2 + j * 55, height / 16 + i * 55),
							new Point(width / 2 + j * 55 + 50, height / 16 + i * 55 + 50)), new Point(i, j));
				}

			}
		}
		return result;
	}

	public boolean doDamage(Player player, Weapon item) {

		Enemies currentEnemyRoom = (Enemies) currentRoom;

		if (currentEnemyRoom.content().isEmpty()) {
			return false;
		}
		Enemy enemy = currentEnemyRoom.content().get(currentEnemyRoom.target());
		enemy.decreaseHP(item.damagePoints());
		if (enemy.isDead()) {
			player.getEXP(Generators.randint(enemy.damage() / 2, enemy.damage() * 2));
			System.out.println(currentEnemyRoom.content());
			currentEnemyRoom.content().remove(enemy);
			currentEnemyRoom.setTarget(0);
		}

		return true;
	}

	public boolean useEnergy(Player player, Weapon item) {
		if (item.isUsable(player.energy())) {
			player.decreaseEnergy(item.energyCost());
			return true;
		}
		return false;
	}

	public boolean hasEnoughEnergy(Player player, int cost) {
		return player.energy() >= cost;
	}

	public boolean doProtect(Player player, Shield item) {
		if (item.isUsable(player.energy())) {
			player.decreaseEnergy(item.energyCost());
			player.increaseDEF(item.protectionPoints());
		}
		return true;
	}

	public void interactItem(Player player, Point xy) {

		Point inventoryItemSelected = xy.getCell(inventory.inventoryCasesToGrid());
		System.out.println("Avant tout : " + currentItemSelected);

		if (currentItemSelected != null) {
			itemSelectedBefore(xy, inventoryItemSelected);
		} else {
			noItemSelectedBefore(player, xy, inventoryItemSelected);
		}
	}

	private void itemSelectedBefore(Point xy, Point inventoryItemSelected) {
		// Rotation de l'item sélectionné (inventaire)
		if (xy.isPointInRectangle(width / 16, 7 * height / 16, width / 16 + 100, 7 * height / 16 + 100)) {
			if (isCurrentItemInventory) {
				currentItemSelected.rotateItem(inventory);
			} else {
				currentItemSelected.rotateItem((ContentRoom) currentRoom);
			}
			return;
		}
		// Une case de l'inventaire est sélectionnée
		if (inventoryItemSelected != null) {
			System.out.println("Un emplacement d'inventaire est choisi");
			inventoryCellSelected(inventoryItemSelected);
		}
		// Suppression de l'item sélectionné (inventaire)
		else if (xy.isPointInRectangle(width / 16 + 200, 7 * height / 16, width / 16 + 280, 7 * height / 16 + 100)) {
			inventory.removeFromInventory(currentItemIDSelected);
		}
		setCurrentItemSelected(null);

	}

	private void inventoryCellSelected(Point inventoryItemSelected) {
		// Si un item est présent dans la case
		int inventoryItemIDSelected = inventory.getID(inventoryItemSelected.i(), inventoryItemSelected.j());
		if (inventoryItemIDSelected != currentItemSelected.id() && inventoryItemIDSelected != 0) {
			System.out.println("On clique sur un item d'inventaire");
			setCurrentItemSelected(inventory.getItem(inventoryItemIDSelected));
			isCurrentItemInventory = true;
		} else {
			insertItem(inventoryItemSelected);
			setCurrentItemSelected(null);
		}
	}

	private void noItemSelectedBefore(Player player, Point xy, Point inventoryItemSelected) {
		if (inventoryItemSelected != null
				&& inventory.getItem(inventoryItemSelected.i(), inventoryItemSelected.j()) != null) {
			setCurrentItemSelected(inventory.getItem(inventoryItemSelected.i(), inventoryItemSelected.j()));
			isCurrentItemInventory = true;
		}
		// Si la salle a du contenu
		else if (!currentRoom.isRoomEmpty()) {
			currentRoomIsNotEmpty(player, xy, (ContentRoom) currentRoom);
		}
	}

	private void currentRoomIsNotEmpty(Player player, Point xy, ContentRoom currentContentRoom) {
		System.out.println("On cherche dans la salle");
		int indexItemSelected = xy.getIndex(currentContentRoom.contentCoordinates());
		if (indexItemSelected == -1)
			return;
		System.out.println("Item de la zone cliquée");
		Item item = (Item) currentContentRoom.content().get(indexItemSelected);

		if (item.isInstantlyUsable()) {
			isItemInstantlyUsable(player, item, (BuyingRoom) currentContentRoom);
		}

		else if (item.isQuantifiable() && (currentRoom.enoughRessources(inventory, item))) {
			directlyAddingQuantifiable((Quantifiable) item, currentContentRoom);
		} else
			setCurrentItemSelected(item);
		isCurrentItemInventory = false;
	}

	private void isItemInstantlyUsable(Player player, Item item, BuyingRoom currentBuyingRoom) {
		if (currentBuyingRoom.enoughRessources(inventory, item)) {
			item.doAction(this, inventory, player);
			currentBuyingRoom.content().remove(item);
			currentBuyingRoom.payPrice(inventory, item);
		}
		setCurrentItemSelected(null);
	}

	private void directlyAddingQuantifiable(Quantifiable item, ContentRoom currentContentRoom) {
		if (inventory.addToSame(item)) {
			currentContentRoom.content().remove(item);
			inventory.payPrice(currentContentRoom, item);
		} else
			setCurrentItemSelected(item);
	}

	private void insertItem(Point xy) {

		System.out.println("-\nTentative d'insertion");

		if (isCurrentItemInventory) {
			inventory.insert(xy.i(), xy.j(), currentItemSelected);
			return;
		}
		ContentRoom currentContentRoom = (ContentRoom) currentRoom;
		if (currentContentRoom.enoughRessources(inventory, currentItemSelected)
				&& inventory.insert(xy.i(), xy.j(), currentItemSelected)) {
			inventory.payPrice(currentContentRoom, currentItemSelected);
			currentContentRoom.content().remove(currentItemSelected);

		}
	}

	public void treasureChest(Player player, ContentRoom currentContentRoom) {
		if (currentContentRoom.isRoomEmpty()) {
			player.setInteractionStatus(false);
		}
	}

	public void enemies(Player player, Point xy, Enemies currentEnemiesRoom) {

		Point clickedCase = xy.getCell(inventory.inventoryCasesToGrid());

		int indexEnemySelected = xy.getIndex(currentEnemiesRoom.contentCoordinates());
		if (indexEnemySelected != -1) {
			currentEnemiesRoom.setTarget(indexEnemySelected);
			return;
		}

		if (xy.isPointInRectangle(width / 2 - 5, 2 * height / 5 - 67, width / 2 - 5 + 300, 2 * height / 5 - 67 + 100)) {
			player.setEnergy(3);
			inventory.inventoryPassiveEffects(player, true);
			player.applyEffects();
			for (Enemy enemy : currentEnemiesRoom.content()) {
				enemy.doAction(player);
				player.setDEF(0);
			}
		}

		if (clickedCase == null) {
			return;
		}

		Item itemSelected = inventory.getItem(clickedCase.i(), clickedCase.j());
		// SI UN ITEM EST SELECTIONNE
		if (itemSelected != null && itemSelected.isUsableInFight()) {
			itemSelected.doAction(this, inventory, player);
		}

		if (currentRoom.isRoomEmpty()) {
			player.enemiesSlain(currentEnemiesRoom);
			updateCurrentRoom(player);
		}
	}

	public void keyDoor(Player player) {
		if (inventory.use("Key")) {

			Room newRoom;
			switch (Generators.randint(2)) {
			case 0:
				newRoom = Treasure.generateTreasure();
				break;
			case 1:
				newRoom = Shop.generateShop();
				break;
			case 2:
				newRoom = Enemies.generateEnemies();
				break;
			default:
				newRoom = new Corridor();
				break;
			}

			player.changeRoom(newRoom);
			updateCurrentRoom(player);
		}
	}

	public Inventory inventory() {
		return inventory;
	}

	public boolean updateFloor(Player player) {
		if (player.updateFloor())
			return true;

		setfloorCellsToGrid(initiateFloorScreen(player.currentFloor()));
		return false;

	}

}