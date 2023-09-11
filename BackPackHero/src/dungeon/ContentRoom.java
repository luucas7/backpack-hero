package dungeon;

import java.util.HashMap;
import java.util.List;
import game.Inventory;
import items.Item;
import utility.Rectangle;

public interface ContentRoom extends InteractiveRoom {

	HashMap<Rectangle, Integer> contentCoordinates();

	void addContentCoordinates(Rectangle a, Integer i);
	List<?> content();

	@Override
	default boolean isRoomEmpty() {
		return content().isEmpty();
	}
	
	default void payPrice(Inventory inventory, Item item) {
		
	}

}
