package dungeon;

import java.util.List;
import items.Item;

public interface BuyingRoom extends ContentRoom {

	@Override
	List<Item> content();

	int price(int i);


}
