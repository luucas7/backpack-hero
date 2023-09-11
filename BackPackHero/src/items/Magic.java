package items;

public interface Magic extends Item {
	int manaCost();
	
	default boolean isCastPossible(int manaCount) {
		return manaCost() <= manaCount;
	}
}
