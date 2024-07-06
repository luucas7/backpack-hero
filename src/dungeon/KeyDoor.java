package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameModel;
import game.GameView;
import living.Player;
import utility.Point;

public class KeyDoor implements InteractiveRoom {
	private final Color color = new Color(182, 143, 64);
	private static final String imageID = "keyDoor.png";

	public KeyDoor() {
	}

	@Override
	public String id() {
		return imageID;
	}

	@Override
	public Color color() {
		return color;
	}

	@Override
	public boolean isVisited() {
		return false;
	}

	public void justVisited() {

	}

	@Override
	public boolean hasItemClickingPriority() {
		return true;
	}

	@Override
	public void doAction(GameModel data, Player player, Point xy) {
		data.keyDoor(player);
	}

	@Override
	public void drawRoom(Graphics2D graphics, GameView view) {

	}

}
