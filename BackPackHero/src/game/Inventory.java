package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dungeon.ContentRoom;
import items.Item;
import items.ItemShape;
import items.Passive;
import items.Quantifiable;
import living.PassiveEffect;
import living.Player;
import utility.Point;
import utility.Rectangle;

public class Inventory {
	private final HashMap<Integer, Item> items = new HashMap<>();
	private final int[][] inventory;
	private HashMap<Rectangle, Point> floorCellsToGrid;
	private HashMap<Rectangle, Point> inventoryCasesToGrid;

	public Inventory() {

		this.inventory = newInventory();
		this.floorCellsToGrid = new HashMap<>();
		this.inventoryCasesToGrid = new HashMap<>();
	}

	public int[][] newInventory() {
		// Starting with a 3x3 grid for better difficulty
		return new int[][] { { -2, -2, -1, -1, -1, -2, -2 }, { -2, -1, 0, 0, 0, -1, -2 }, { -2, -1, 0, 0, 0, -1, -2 },
				{ -2, -1, 0, 0, 0, -1, -2 }, { -2, -2, -1, -1, -1, -2, -2 } };
	}

	public void startGame(int itemSize, int marginI, int marginY, Item... items) {

		setinventoryCasesToGrid(initiateInventoryScreen(itemSize, marginI, marginY));

		for (int i = 0; i < items.length; i++) {
			insert(1, 2 + i, items[i]);
		}

	}

	public void unlockSlot(Player player, int i, int j) {

		inventory[i][j] = 0;
		makeNeighborsUnlockable(i, j);
		player.decreaseSlotsUnlockable();
	}

	// Set all the neighbors cells to unlockable status
	public void makeNeighborsUnlockable(int i, int j) {
		for (Point neighbor : new Point[] { new Point(i + 1, j), new Point(i, j + 1), new Point(i - 1, j),
				new Point(i, j - 1) }) {
			int x = neighbor.i();
			int y = neighbor.j();
			if (x < 0 || x >= lines() || y < 0 || y >= columns() || inventory[x][y] != -2) {
				continue;
			}
			inventory[x][y] = -1;
		}
	}

	public HashMap<Rectangle, Point> floorCellsToGrid() {
		return floorCellsToGrid;
	}

	public HashMap<Rectangle, Point> inventoryCasesToGrid() {
		return inventoryCasesToGrid;
	}

	public void setfloorCellsToGrid(HashMap<Rectangle, Point> map) {
		this.floorCellsToGrid = map;
	}

	public void setinventoryCasesToGrid(HashMap<Rectangle, Point> map) {
		this.inventoryCasesToGrid = map;
	}

	public int lines() {
		return inventory.length;
	}

	public int columns() {
		return inventory[0].length;
	}

	public int getID(int i, int j) {
		return inventory[i][j];
	}

	public Item getItem(int i, int j) {
		return items.get(getID(i, j));
	}

	public Item getItem(int id) {
		return items.get(id);
	}

	public boolean insert(int i, int j, Item item) {
		if (!isShapeInsertPossible(i, j, item.shape(), item.id())) {
			return false;
		}
		return insertInInventory(i, j, item);
	}

	public boolean isShapeInsertPossible(int i, int j, ItemShape pattern, int id) {
		int[][] shape = pattern.positions();
		int verticalSize = pattern.getVerticalSize();
		int horizontalSize = pattern.getHorizontalSize();

		if (i + verticalSize > inventory.length || j + horizontalSize > inventory[0].length || i < 0 || j < 0) {
			return false;
		}
		for (int x = 0; x < verticalSize; x++) {
			for (int y = 0; y < horizontalSize; y++)
				if (shape[x][y] == 1 && inventory[x + i][y + j] != 0 && inventory[x + i][y + j] != id) {
					return false;
				}
		}
		return true;
	}

	public boolean removeFromInventory(int id) {
		boolean wasThere = false;
		items.remove(id);
		for (int x = 0; x < inventory.length; x++) {
			for (int y = 0; y < inventory[0].length; y++)
				if (inventory[x][y] == id) {
					wasThere = true;
					inventory[x][y] = 0;
				}
		}
		return wasThere;
	}

	public boolean insertInInventory(int i, int j, Item item) {
		int verticalSize = item.shape().getVerticalSize();
		int horizontalSize = item.shape().getHorizontalSize();

		removeFromInventory(item.id());
		for (int x = 0; x < verticalSize; x++) {
			for (int y = 0; y < horizontalSize; y++) {
				if (item.shape().positions()[x][y] == 1)
					inventory[x + i][y + j] = item.id();
			}
		}
		items.put(item.id(), item);
		return true;
	}

	public boolean isRotatingPossible(int id) {
		Item item = items.get(id);

		Point coordinates = getItemCoordinates(id);

		if (coordinates == null) {
			return false;
		}

		ItemShape newShape = item.shape().rotateShape();
		if (!isShapeInsertPossible(coordinates.i(), coordinates.j(), newShape, id)) {
			return false;
		}

		item.setShape(newShape);
		return insertInInventory(coordinates.i(), coordinates.j(), item);

	}

	public Point getItemCoordinates(int id) {
		for (int i = 0; i < inventory.length; i++) {
			for (int j = 0; j < inventory[0].length; j++) {
				if (inventory[i][j] == id) {

					Item item = items.get(id);
					int dj = item.shape().getTopLeftCorner();

					return new Point(i, j - dj);
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		var res = new StringBuilder();

		for (int i = 0; i < inventory.length; i++) {
			for (int j = 0; j < inventory[0].length; j++) {
				res.append(inventory[i][j]).append(" - ");
			}
			res.append("\n");
		}
		return res.toString();
	}

	public HashMap<Rectangle, Point> initiateInventoryScreen(int itemSize, int marginI, int marginY) {
		var result = new HashMap<Rectangle, Point>();
		for (int i = 0; i < lines(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.put(
						new Rectangle(new Point(j * itemSize + marginY, i * itemSize + marginI),
								new Point(j * itemSize + marginY + itemSize, i * itemSize + marginI + itemSize)),
						new Point(i, j));

			}
		}

		return result;
	}

	public boolean isItemInInventory(Item item) {
		Objects.requireNonNull(item);
		return items.values().contains(item);
	}

	public Item hasSameItem(Item item) {
		Objects.requireNonNull(item);
		for (Item maybeSameItem : items.values()) {
			if (item.name().equals(maybeSameItem.name())) {
				return maybeSameItem;
			}
		}
		return null;
	}

	public boolean addToSame(Quantifiable item) {
		Objects.requireNonNull(item);

		Quantifiable itemToAddUp = (Quantifiable) hasSameItem(item);
		if (itemToAddUp == null) {
			return false;
		}

		itemToAddUp.addToCount(item.count());
		return true;

	}

	public List<PassiveEffect> inventoryPassiveEffects(Player player, boolean activate) {
		Objects.requireNonNull(player);
		List<PassiveEffect> effectList = new ArrayList<>();
		for (Item maybeHasPassive : items.values()) {
			if (maybeHasPassive.isPassive()) {
				Passive hasPassive = (Passive) maybeHasPassive;
				if (activate) {
					hasPassive.activatePassive(player);
				}
				effectList.add(hasPassive.effect());
			}
		}
		return effectList;
	}

	public boolean payPrice(ContentRoom room, Item item) {
		Objects.requireNonNull(room);
		System.out.println(item.price());
		if (item.price() == 0) {
			return true;
		}

		if (enoughRessources(item.price())) {
			useRessource(item.price());
			room.content().remove(item);
			return true;
		}
		return false;
	}

	public boolean useRessource(int count) {
		return useQuantifiable("Gold", count);
	}

	public boolean enoughRessources(int count) {
		return enoughRessources("Gold", count);
	}

	public boolean enoughRessources(String name, int count) {
		Objects.requireNonNull(name);
		for (Item maybeIt : items.values()) {
			if (maybeIt.name().contains(name)) {
				Quantifiable itemToDrop = (Quantifiable) maybeIt;
				return (itemToDrop.count() >= count);
			}
		}
		return false;
	}

	public boolean useQuantifiable(String name) {
		return useQuantifiable(name, 1);
	}

	public boolean useQuantifiable(String name, int count) {
		Objects.requireNonNull(name);
		for (Item maybeIt : items.values()) {
			if (maybeIt.name().contains(name)) {
				Quantifiable itemToDrop = (Quantifiable) maybeIt;
				itemToDrop.useMultiples(this, count);
				return true;
			}
		}
		return false;
	}

	public boolean use(String name) {
		Objects.requireNonNull(name);
		for (Item maybeIt : items.values()) {
			if (maybeIt.name().contains(name)) {
				return removeFromInventory(maybeIt.id());
			}
		}
		return false;

	}

	public int calculateInventoryScore() {
		int res = 0;
		for (Item item : items.values()) {
			res += (item.rarity()+1) * 7;
		}
		return res;
	}

	public void unlockingNewSlot(Player player, Point slot) {
		int i = slot.i();
		int j = slot.j();
		if (getID(i, j) == -1 && player.areSlotsUnlockable()) {
			unlockSlot(player, i, j);
		}
	}

}
