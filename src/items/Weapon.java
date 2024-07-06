package items;

public interface Weapon extends Item{

	
	int energyCost();
	int damagePoints();
	
	default boolean isUsable(int playerEnergy) {
		return energyCost() <= playerEnergy;
	}
	
	
	
}
