package items;

import game.Inventory;

public interface Quantifiable extends Item {

	void dropOne();

	int count();
	int addToCount(int toAdd);

	@Override
	default boolean isQuantifiable() {
		return true;
	}

	default boolean useOne(Inventory inventory) {
		dropOne();
		if (count() <= 0) {
			inventory.removeFromInventory(id());
		}
		return count() >= 0;
	}
	
	default void useMultiples(Inventory inventory, int count) {
		addToCount(-count);
		if (count() <= 0) {
			inventory.removeFromInventory(id());
		}
	}

	default boolean isSameItem(Quantifiable item) {
		return name().equals(item.name());
	}

}
