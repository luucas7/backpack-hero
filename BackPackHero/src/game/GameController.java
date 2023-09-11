package game;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.IOException;
import dungeon.Floor;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event.Action;
import items.Item;
import living.Player;
import utility.Point;
import fr.umlv.zen5.KeyboardKey;

public class GameController {
	private static final int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private static final int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public static int width() {
		return width;
	}

	public static int height() {
		return height;
	}

	private static boolean gameLoop(ApplicationContext context, GameModel data, GameView view, Player player) {
		var event = context.pollOrWaitEvent(10);
		if (event == null) {
			return true;
		}
		var action = event.getAction();

		if (action == Action.KEY_PRESSED && event.getKey() == KeyboardKey.UNDEFINED) {
			return false;
		}

		if (action != Action.POINTER_DOWN) {
			return true;
		}

		System.out.println("-----");
		var location = event.getLocation();
		int x = (int) location.x;
		int y = (int) location.y;
		Point xy = new Point(x, y);

		// Moving in the current floor
		Point clickedCell = xy.getCell(data.floorCellsToGrid());
		// If movingTo returns True then the game ends
		if (clickedCell != null && (player.movingTo(data, clickedCell))) {
			return false;
		}

		// If the player clicks in an unlockable slot
		Point clickedSlot = xy.getCell(data.inventory().inventoryCasesToGrid());
		if (clickedSlot != null)
			data.inventory().unlockingNewSlot(player, clickedSlot);

		// If the room can let you interact with items
		if (!data.currentRoom().hasItemClickingPriority()) {
			data.interactItem(player, xy);
		} else {
			// If the room requires special interactions with items (enemies rooms)
			data.currentRoom().doAction(data, player, xy);
		}

		view.draw(context, data, player);
		return !player.isDead();
	}


	private static void backpackhero(ApplicationContext context) throws IOException {

		ImageLoader images = ImageLoader.createLoader("data");

		GameData gameData = new GameData(images);
		Item[] startItems = gameData.items();

		gameData.enemies();
		int itemSize = 70;
		int marginI = 30;
		int marginY = 110;
		GameView view = new GameView(images, itemSize, marginI, marginY);
		GameModel data = new GameModel();

		Player player = new Player(Floor.generateFloors(3));

		data.startGame(player, itemSize, marginI, marginY, startItems);

		view.draw(context, data, player);
		while (true) {
			if (!gameLoop(context, data, view, player)) {
				if (player.didWin()) EndOfTheGame.endGame(context, data, view, player);
				context.exit(0);
				return;
			}
		}
	}

	public static void main(String[] args) {
		Application.run(new Color(71, 68, 72), t -> {
			try {
				backpackhero(t);
			} catch (IOException e) {
				e.printStackTrace();

			}
		});
	}
}