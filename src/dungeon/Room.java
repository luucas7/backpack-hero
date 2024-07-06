package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameModel;
import game.GameView;
import game.Inventory;
import items.Item;
import living.Player;
import utility.Point;

public interface Room {
	
	String id();
	Color color();

	boolean isVisited();
	void justVisited();
	default boolean isBlocking() {
		return false;
	}
	
	
	default boolean hasAnInteraction() {
		return false;
	}
	
	default boolean isRoomEmpty() {
		return true;
	}
	
	default boolean isAnExit() {
		return false;
	}
	
	default boolean enoughRessources(Inventory inventory, Item item) {
		return true;
	}
	
	// To block item moving in fight
	default boolean hasItemClickingPriority() {
		return false;
	}
	
	default void doAction(GameModel data, Player player, Point xy) {
		return;
	}
	
	void drawRoom(Graphics2D graphics, GameView view);
	
	
	

}
