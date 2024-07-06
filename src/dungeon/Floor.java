package dungeon;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import utility.Generators;
import utility.Point;

public class Floor {
	private final Room[][] floorGrid;
	private final int iStart;
	private final int jStart;

	public Floor(Room[][] rooms, int iStart, int jStart) {
		this.floorGrid = Objects.requireNonNull(rooms);
		this.iStart = iStart;
		this.jStart = jStart;
	}

	public Room[][] floorGrid() {
		return floorGrid;
	}

	public int lines() {
		return floorGrid.length;
	}

	public int columns() {
		return floorGrid[0].length;
	}

	public int iStart() {
		return iStart;
	}

	public int jStart() {
		return jStart;
	}

	public Room getFirstRoom() {
		return floorGrid[iStart][jStart];
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("\n");
		for (int i = 0; i < lines(); i++) {
			for (int j = 0; j < columns(); j++) {
				res.append(floorGrid[i][j]).append(" ");
			}
			res.append("\n");
		}
		return res.toString();
	}

	public void changeRoom(Room newRoom, int i, int j) {
		if (i < 0 || i >= lines() || j < 0 || j >= columns())
			return;
		floorGrid[i][j] = newRoom;
	}

	public boolean isAccessible(int i, int j) {
		if ((i < 0 || i >= floorGrid.length) || (j < 0 || j >= floorGrid[0].length)) {
			return false;
		}

		return (floorGrid[i][j] == null);
	}

	public boolean isAccessible(Point coordinates) {
		return isAccessible(coordinates.i(), coordinates.j());
	}

	public boolean pathTo(int i, int j, int iFinal, int jFinal) {
		return pathTo(i, j, iFinal, jFinal, new HashSet<>());
	}

	private boolean pathTo(int i, int j, int iFinal, int jFinal, HashSet<Point> visited) {

		if (i == iFinal && j == jFinal) {
			return true;

		}
		if (floorGrid[i][j] == null || !floorGrid[i][j].isVisited() && floorGrid[i][j].isBlocking()) {
			return false;
		}

		if (visited.contains(new Point(i, j))) {
			return false;
		}

		visited.add(new Point(i, j));

		boolean found = false;

		if (i > 0) {
			found = pathTo(i - 1, j, iFinal, jFinal, visited);
		}
		if (!found && i < lines() - 1) {
			found = pathTo(i + 1, j, iFinal, jFinal, visited);
		}
		if (!found && j > 0) {
			found = pathTo(i, j - 1, iFinal, jFinal, visited);
		}
		if (!found && j < columns() - 1) {
			found = pathTo(i, j + 1, iFinal, jFinal, visited);
		}

		if (found) {
			return true;
		} else {
			visited.remove(new Point(i, j));
			return false;
		}
	}

	public void justVisited(int i, int j) {
		floorGrid[i][j].justVisited();
	}

	public static Floor[] generateFloors(int nbFloors) {
		Floor[] floors = new Floor[nbFloors];

		for (int i = 0; i < nbFloors; i++) {
			floors[i] = generateFloor();
		}

		return floors;
	}

	public static Floor generateFloor() {
		int iStart = Generators.randint(4);
		Floor rooms = new Floor(new Room[5][11], iStart, 0);
		generateFloor(rooms, iStart, 0);
		completeHoles(rooms);
		putRooms(rooms);
		getARandomEnemiesAndGiveKey(rooms);
		return rooms;
	}

	// Give a key to an Enemies' content
	private static void getARandomEnemiesAndGiveKey(Floor rooms) {
		int i = 1;
		int j = 1;

		while (rooms.roomAt(i, j) == null || !rooms.roomAt(i, j).isBlocking()) {
			i = Generators.randint(4);
			j = Generators.randint(1, 10);
		}
		((Enemies) rooms.roomAt(i, j)).gotAKey();
	}

	// Set up the interactive rooms
	private static void putRooms(Floor rooms) {
		int treasureCount = 2;
		int healerCount = 1;
		int shopCount = 1;

		// Set exit
		for (int i = 0; i < 5; i++) {
			if (rooms.roomAt(i, 10) != null && !rooms.roomAt(i, 10).isAnExit()) {
				rooms.changeRoom(new Exit(), i, 10);
				break;
			}
		}

		int enemiesCount = setUpMandatoryEnemies(rooms);

		setUpRooms(rooms, enemiesCount, Enemies::generateEnemies, false);
		setUpRooms(rooms, treasureCount, Treasure::generateTreasure, false);
		setUpRooms(rooms, shopCount, Shop::generateShop, true);
		setUpRooms(rooms, healerCount, Healer::generateHealer, true);
		setUpRooms(rooms, 1, KeyDoor::new, true);
	}

	private static <T extends Room> void setUpRooms(Floor rooms, int count, Supplier<T> function, boolean right) {
		for (int i = count; i > 0; i--) {
			Point randomPoint = getARandomCorridor(rooms, right);
			rooms.changeRoom(function.get(), randomPoint.i(), randomPoint.j());
		}
	}

	// Creating a column of enemies/key door to ensure that we are blocked
	private static int setUpMandatoryEnemies(Floor rooms) {

		boolean isThereKeyDoor = false;
		int enemiesCount = Generators.randint(3, 4);
		int indexWall = 0;
		int minRoomsCount = 6;
		for (int j = 1; j < 10; j++) {
			int roomsCount = 0;
			for (int i = 0; i < 5; i++) {
				if ((rooms.roomAt(i, j) != null) && !rooms.roomAt(i, j).hasAnInteraction())
					roomsCount++;
			}
			if (minRoomsCount >= roomsCount) {
				minRoomsCount = roomsCount;
				indexWall = j;
			}
		}
		
		for (int i = 0; i < 5; i++) {
			if ((rooms.roomAt(i, indexWall) != null) && !(rooms.roomAt(i, indexWall).hasAnInteraction())) {

				if ( !isThereKeyDoor && (minRoomsCount-enemiesCount)>=1) {
					rooms.changeRoom(new KeyDoor(), i, indexWall);
					isThereKeyDoor = true;
					
				}
				else {
					rooms.changeRoom(Enemies.generateEnemies(), i, indexWall);
					enemiesCount--;
				}
			}
		}
		return enemiesCount;
	}

	private static Point getARandomCorridor(Floor floor, boolean right) {
		int i = 1;
		int j = 1;

		while (floor.roomAt(i, j) == null || floor.roomAt(i, j).hasAnInteraction()) {
			i = Generators.randint(4);
			j = right ? Generators.randint(5, 10) : Generators.randint(1, 10);
		}
		return new Point(i, j);
	}

	private static void generateFloor(Floor floor, int i, int j) {

		if (j > 10) {
			return;
		}

		if (i < 0) {
			generateFloor(floor, 0, j);
			return;
		}
		if (i > 4) {
			generateFloor(floor, 4, j);
			return;
		}

		floor.changeRoom(new Corridor(), i, j);

		int a = Generators.randint(0, 2);

		if (a == 0) {

			floor.changeRoom(new Corridor(), i - 1, j);
			generateFloor(floor, i - 1, j);
		}
		if (a == 1) {
			floor.changeRoom(new Corridor(), i, j + 1);
			generateFloor(floor, i, j + 1);
		}
		if (a == 2) {
			floor.changeRoom(new Corridor(), i + 1, j);
			generateFloor(floor, i + 1, j);
		}

	}

	private static void completeHoles(Floor rooms) {
		for (int i = 1; i < 5; i++) {
			for (int j = 1; j < 11; j++) {
				if (!isRoomSurrounded(rooms, i, j)) {
					generateFloor(rooms, i <= 2 ? i + 1 : i - 1, j);
				}
			}
		}
	}

	private static boolean isRoomSurrounded(Floor rooms, int i, int j) {

		List<Point> liste = List.of(new Point(i + 1, j), new Point(i - 1, j), new Point(i - 1, j - 1),
				new Point(i + 1, j + 1), new Point(i, j - 1), new Point(i, j + 1), new Point(i + 1, j - 1),
				new Point(i - 1, j + 1));
		for (Point cell : liste) {
			int di = cell.i();
			int dj = cell.j();
			if (!(di < 0 || di > 4 || dj < 0 || dj > 10) && (rooms.roomAt(di, dj) != null)) {
				return true;
			}
		}
		return false;
	}

	private Room roomAt(int di, int dj) {
		return floorGrid[di][dj];
	}

}
