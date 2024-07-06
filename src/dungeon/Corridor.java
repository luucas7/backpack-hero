package dungeon;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameView;

public record Corridor() implements Room {

	@Override
	public String id() {
		return null;
	}

	@Override
	public Color color() {
		return new Color(128, 135, 130);
	}
	

	@Override
	public boolean isVisited() {
		return false;
	}
	
	public void justVisited() {
	}
	
	@Override
	public void drawRoom(Graphics2D graphics, GameView view) {
	}
	
	@Override
	public String toString() {
		return "cccc";
	}
}
