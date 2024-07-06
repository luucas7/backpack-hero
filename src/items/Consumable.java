package items;

public interface Consumable extends Item {

	@Override
	default boolean isConsumable() {
		return true;
	}

}
