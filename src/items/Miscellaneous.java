package items;

public interface Miscellaneous extends Item {

	
	@Override
	default boolean isUsableInFight() {
		return false;
	}
	
}
