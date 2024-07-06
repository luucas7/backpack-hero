package game;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.KeyboardKey;
import fr.umlv.zen5.Event.Action;
import living.Player;

public class EndOfTheGame {
	private int score;

	public EndOfTheGame(int score) {
		this.score = score;
	}

	public int score() {
		return score;
	}


	private static int calculatePlayerScore(Player player) {
		int res = player.maxHealth() * 4;
		res += player.exp() * 2;
		return res;
	}

	public static int finalScore(Player player, Inventory inventory) {
		return calculatePlayerScore(player) + inventory.calculateInventoryScore();
	}
	
	public static void endGame(ApplicationContext context, GameModel data, GameView view, Player player) {
		boolean backgroundIsDrawn = false;
		StringBuilder name = new StringBuilder();
		while (true) {
			var event = context.pollOrWaitEvent(10);
			if (event == null) {
				continue;
			}
			var action = event.getAction();

			if (action == Action.KEY_PRESSED && (event.getKey() == KeyboardKey.UNDEFINED) && name.length() >= 1) {
				name.deleteCharAt(name.length() - 1);
			}

			if (action == Action.KEY_PRESSED && (event.getKey() == KeyboardKey.SPACE)) {

				writeScore(Path.of("data/scores.txt"),(finalScore(player, data.inventory()) + ": " + name).toLowerCase());
				return;

			}
			String letter = getLetter(event);
			if (letter != null && name.length() <= 10)
				name.append(letter);

			view.drawEnd(context, name.toString(), backgroundIsDrawn, finalScore(player, data.inventory()));
			backgroundIsDrawn = true;

		}
	}

	private static void writeScore(Path dest, String name) {
		DateFormat dateFormat = new SimpleDateFormat(", EEEEE dd MMM yyyy");
		String score = name + dateFormat.format(Calendar.getInstance().getTime());

		try (var output = Files.newBufferedWriter(dest, StandardOpenOption.APPEND)) {
			output.write(score);
			output.newLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String getLetter(Event event) {
		if (event.getAction() == Action.KEY_PRESSED) {
			var key = event.getKey().describeConstable();

			if (!key.isEmpty()) {
				String letter = key.get().constantName();
				if (letter.matches("^[a-zA-Z0-9]$")) {
					return letter;
				}
			}

		}
		return null;
	}

}
