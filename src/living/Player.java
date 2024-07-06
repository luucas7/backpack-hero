package living;

import java.util.HashMap;
import java.util.List;
import dungeon.Enemies;
import dungeon.Floor;
import dungeon.Room;
import game.GameModel;
import utility.Generators;
import utility.Point;

public class Player implements Living {
	private int maxHealth = 40;
	private int health = maxHealth;
	private int energy = 3;
	private int defending = 0;
	private int level = 0;
	private int exp = 0;
	private int slotsUnlocked = 0;
	private final int[] levelCap = new int[] { 0, 8, 26, 44, 86, 142, 200, 280, 380, 500, 640, 800, 980, 1180 };
	private boolean isInteracting = false;
	// Enemy or Item ID -> It's effect
	private final HashMap<Integer, PassiveEffect> effects = new HashMap<>();

	private int i;
	private int j;
	private List<Floor> floors;
	private int currentFloorIndex = 0;

	public Player(Floor... floors) {
		this.floors = List.of(floors);
		this.i = currentFloor().iStart();
		this.j = currentFloor().jStart();
	}

	public int health() {
		return health;
	}

	public int energy() {
		return energy;
	}

	public int i() {
		return i;
	}

	public int j() {
		return j;
	}

	public int level() {
		return level;
	}

	public int exp() {
		return exp;
	}

	public void setLevel(int newLevel) {
		int dlevel = newLevel - level;
		level = newLevel;

		for (int i = 0; i < dlevel; i++) {
			int nb = Generators.randint(3, 4);
			slotsUnlocked += nb;
			maxHealth += nb * 2;
			increaseHP(nb * 2 - 1);
		}
	}

	public boolean areSlotsUnlockable() {
		return slotsUnlocked > 0;
	}

	public void decreaseSlotsUnlockable() {
		slotsUnlocked--;
	}

	public void getEXP(int dexp) {
		this.exp += dexp;
		int newLevel = getLevelFromEXP();
		if (newLevel != level) {
			setLevel(newLevel);
		}
	}

	public int getLevelFromEXP() {
		for (int dexp = levelCap.length - 1; dexp >= 0; dexp--) {
			if (levelCap[dexp] <= exp)
				return dexp;

		}
		return 0;
	}

	public int getRemainingEXPToLevelUp() {
		return levelCap[level + 1] - levelCap[level];
	}

	public int getTotalEXP() {
		return levelCap[level];
	}

	public Floor currentFloor() {
		return floors.get(currentFloorIndex);
	}

	public Room getRoom(int i, int j) {
		return currentFloor().floorGrid()[i][j];
	}

	public boolean didWin() {
		return currentFloorIndex  >= floors.size();
	}
	
	public boolean updateFloor() {
		this.currentFloorIndex++;
		if (currentFloorIndex >= floors.size())
			return true;
		updateStart();
		return false;

	}

	public void updateStart() {
		this.i = currentFloor().iStart();
		this.j = currentFloor().jStart();
	}

	public boolean isInteracting() {
		return isInteracting;
	}

	public void changeRoom(Room newRoom) {
		currentFloor().changeRoom(newRoom, i, j);
	}

	@Override
	public void decreaseHP(int damage) {
		if (damage >= defending) {
			health = health - (damage - defending);
			defending = 0;
		} else {
			defending -= damage;
		}
	}

	public void decreaseHPthroughDEF(int damage) {
		health -= damage;
	}

	@Override
	public void increaseHP(int hp) {
		this.health = Math.min(maxHealth, health + hp);
	}

	@Override
	public void decreaseDEF(int def) {
		this.defending -= def;
	}

	@Override
	public void increaseDEF(int def) {
		this.defending += def;
	}

	public void decreaseEnergy(int energyCost) {
		this.energy -= energyCost;
	}

	public boolean moveTo(int iFinal, int jFinal) {
		if (!currentFloor().isAccessible(iFinal, jFinal) && !(currentFloor().pathTo(i, j, iFinal, jFinal))) {
			return false;
		}
		i = iFinal;
		j = jFinal;
		return true;
	}

	public void setInteractionStatus(boolean state) {
		this.isInteracting = state;
	}

	public void setEnergy(int newEnergy) {
		this.energy = newEnergy;
	}

	public void setDEF(int i) {
		this.defending = i;
	}

	public int maxHealth() {
		return maxHealth;
	}

	public int defending() {
		return defending;
	}

	public void increaseStamina(int stamina) {
		energy += stamina;
	}

	public void addEffect(Integer id, PassiveEffect effect) {
		effects.put(id, effect);
	}

	public void applyEffects() {
		for (PassiveEffect effect : effects.values()) {
			effect.applyEffect(this);
		}
	}

	public List<PassiveEffect> getAllEffects() {
		return List.copyOf(effects.values());
	}

	public void enemiesSlain(Enemies room) {
		setEnergy(3);
		changeRoom(room.setTreasure());
		removeEnemiesEffects();
	}

	public void removeEnemiesEffects() {
		effects.clear();
	}

	

	public boolean movingTo(GameModel data, Point clickedCell) {

		data.setCurrentItemSelected(null);
		// Moving in an enemy room is not allowed
		if (data.currentRoom().isBlocking()) {
			return false;

		}
		data.currentRoom().justVisited();
		moveTo(clickedCell.i(), clickedCell.j());
		var currentRoom = currentFloor().floorGrid()[i][j];

		if (currentRoom.isAnExit()) {
			// If last level
			if (data.updateFloor(this)) {
				return true;
			}
			data.setCurrentRoom(getRoom(i, j));

		}

		// ON SE PLACE DANS LA ROOM POUR LE PROCHAIN EVENEMENT
		if (currentRoom.hasAnInteraction() && !currentRoom.isVisited()) {
			setInteractionStatus(true);
		} else {
			if (!currentRoom.hasAnInteraction()) {
				setInteractionStatus(false);
			}
		}
		data.setCurrentRoom(currentRoom);
		return false;
	}
}
