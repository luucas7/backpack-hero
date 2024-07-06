package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameModel;
import game.GameView;
import living.Player;
import utility.Point;

public class Exit implements InteractiveRoom {
	private final Color color = new Color(158, 178, 93);
	private final String imageID = "exit.png";
	private boolean isVisited = false;
	
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
		return isVisited;
	}

	@Override
	public void justVisited() {
	}
	
	@Override
	public boolean hasItemClickingPriority() {
		return true;
	}

	@Override
	public void doAction(GameModel data, Player player, Point xy) {
		data.setCurrentRoom(player.getRoom(player.i(), player.j()));
	}

	@Override
	public void drawRoom(Graphics2D graphics, GameView view) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isAnExit() {
		return true;
	}



}
