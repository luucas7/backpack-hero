package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import dungeon.BuyingRoom;
import dungeon.Enemies;
import dungeon.Floor;
import dungeon.Room;
import dungeon.Treasure;
import fr.umlv.zen5.ApplicationContext;
import items.Instant;
import items.Item;
import items.ItemShape;
import items.Quantifiable;
import living.Enemy;
import living.PassiveDamageEnemy;
import living.Player;
import utility.Point;
import utility.Rectangle;

public record GameView(ImageLoader loader, int itemSize, int marginI, int marginY, int width, int height, TreeMap<Integer, String> top ) {

	public GameView(ImageLoader loader, int itemSize, int marginI, int marginY, int width, int height, TreeMap<Integer, String> top) {
		this.loader = Objects.requireNonNull(loader);
		this.itemSize = itemSize;
		this.marginI = marginI;
		this.marginY = marginY;
		this.width = width;
		this.height = height;
		this.top = top;
	}

	@Override //Overriding the record accessor
	public TreeMap<Integer, String> top(){
		return (TreeMap<Integer, String>) Map.copyOf(top);
	}
	
	public GameView(ImageLoader loader, int itemSize, int marginI, int marginY) {
		this(loader, itemSize, marginI, marginY, GameController.width(), GameController.height(), new TreeMap<>());
	}

	private void drawImage(Graphics2D graphics, BufferedImage image, float x, float y, float dimX, float dimY,
			boolean doScale) {
		if (!doScale) {
			graphics.drawImage(image, (int) x, (int) y, (int) dimX, (int) dimY, null);
			return;
		}
		var width = image.getWidth();
		var height = image.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
		var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
				y + (dimY - scale * height) / 2);
		graphics.drawImage(image, transform, null);
	}

	private void drawBackground(Graphics2D graphics) {

		// Background
		graphics.setColor(new Color(71, 68, 72));
		graphics.fill(new Rectangle2D.Float(0, 0, width, height));

		drawImage(graphics, loader.image("background.png"), 0, 0, width, height, false);

	}

	private void drawFloor(Graphics2D graphics, Floor currentFloor) {

		// Floor Background
		graphics.setColor(new Color(57, 57, 58));
		graphics.fill(new Rectangle2D.Float(width / 2 - 5, height / 16 - 5, 610, 280));

		var floorGrid = currentFloor.floorGrid();
		Room actualRoom;

		for (int i = 0; i < currentFloor.lines(); i++) {
			for (int j = 0; j < currentFloor.columns(); j++) {
				actualRoom = floorGrid[i][j];
				if (actualRoom != null) {
					graphics.setColor(actualRoom.color());
					graphics.fill(new Rectangle2D.Float(width / 2 + j * 55, height / 16 + i * 55, 50, 50));

					if (actualRoom.hasAnInteraction()) {
						drawImage(graphics, loader.image(actualRoom.id()), (width / 2 + j * 55 + 5),
								(height / 16 + i * 55 + 5), 40, 40, false);

						if (actualRoom.isVisited()) {
							drawImage(graphics, loader.image("checkmark.png"), (width / 2 + j * 55 + 5),
									(height / 16 + i * 55 + 5), 40, 40, false);
						}
					}
				}
			}
		}
	}

	private void drawPlayer(Graphics2D graphics, Player player) {
		drawImage(graphics, loader.image("playerFloor.png"), width / 2 + player.j() * 55,
				height / 16 + player.i() * 55 + 17, 50, 33, true);

		drawImage(graphics, loader.image("player.png"), width / 4, 3 * height / 4 - 156, 153, 187, true);

		// Stamina
		graphics.setFont(new Font("Arial", Font.PLAIN, 40));
		graphics.setColor(new Color(143, 247, 167));
		graphics.drawString("%d/3 stamina".formatted(player.energy()), width / 4, height / 2);

		// HP
		graphics.setFont(new Font("Arial", Font.PLAIN, 30));

		graphics.setColor(new Color(0, 0, 0));
		graphics.fill(new Rectangle2D.Float(width / 4 - 11, 3 * height / 4 + 59, 172, 42));

		graphics.setColor(new Color(160, 42, 51));
		graphics.fill(new Rectangle2D.Float(width / 4 - 10, 3 * height / 4 + 60,
				player.health() * 170 / player.maxHealth(), 40));

		graphics.setColor(new Color(255, 255, 255));
		graphics.drawString("%d HP".formatted(player.health()), width / 4 + 15, 3 * height / 4 + 90);

		// DEF
		graphics.setColor(new Color(110, 114, 113));
		graphics.drawString("%d DEF".formatted(player.defending()), width / 4 + 15, 3 * height / 4 + 135);
		
		// level
		graphics.setColor(new Color(0, 0, 0));
		graphics.fill(new Rectangle2D.Float(width / 7 - 11, 3 * height / 4 + 59, 172, 42));

		graphics.setColor(new Color(63, 124, 172));
		graphics.fill(new Rectangle2D.Float(width / 7 - 10, 3 * height / 4 + 60,
				(player.exp() - player.getTotalEXP()) * 190 / player.getRemainingEXPToLevelUp(), 40));

		graphics.setColor(new Color(255, 255, 255));
		graphics.drawString("LVL %d".formatted(player.level()+1), width / 7 + 15, 3 * height / 4 + 90);

	}

	private void drawItem(Graphics2D graphics, Inventory inventory, int i, int j) {
		Item item = (inventory.getItem(i, j));
		BufferedImage image = item.image();
		ItemShape itemShape = item.shape();
		int maybeNeedToSetCorner = itemShape.getTopLeftCorner();

		int imageWidth = (itemShape.getHorizontalSize()) * itemSize;
		int imageHeight = (itemShape.getVerticalSize()) * itemSize;

		drawImage(graphics, image, (j - maybeNeedToSetCorner) * itemSize + marginY, i * itemSize + marginI, imageWidth,
				imageHeight, true);

		if (item.isQuantifiable()) {
			graphics.setFont(new Font("Arial", Font.ROMAN_BASELINE, 30));
			graphics.setColor(new Color(255, 255, 255));
			graphics.drawString("%d".formatted(((Quantifiable) item).count()),
					(j - maybeNeedToSetCorner) * itemSize + marginY + 2 * itemSize / 3, (1 + i) * (itemSize) + marginI);

		}
	}

	private Rectangle drawItem(Graphics2D graphics, Item item, int i, int j) {
		BufferedImage image = item.image();

		int imageWidth = item.shape().getHorizontalSize() * itemSize;
		int imageHeight = item.shape().getVerticalSize() * itemSize;
		drawImage(graphics, image, i, j, imageWidth, imageHeight, true);

		return new Rectangle(new Point(i, j), new Point(i + imageWidth, j + imageHeight));

	}

	private void drawInventoryBack(Graphics2D graphics, Inventory inventory, Player player) {

		drawImage(graphics, loader.image("backpack.png"), 0, 0, itemSize * 10, itemSize * 6, false);

		for (int i = 0; i < inventory.lines(); i++) {
			for (int j = 0; j < inventory.columns(); j++) {
				if (inventory.getID(i, j) > -1) {
					drawImage(graphics, loader.image("shape.png"), j * itemSize + marginY, i * itemSize + marginI,
							itemSize, itemSize, true);

				} else if (player.areSlotsUnlockable() && inventory.getID(i, j) == -1) {
					drawImage(graphics, loader.image("shape.png"), j * itemSize + marginY, i * itemSize + marginI,
							itemSize, itemSize, true);
					drawImage(graphics, loader.image("plus.png"), j * itemSize + marginY, i * itemSize + marginI,
							itemSize, itemSize, true);
				}
			}
		}
	}

	private void drawInventory(Graphics2D graphics, Inventory inventory, Player player) {
		drawInventoryBack(graphics, inventory, player);
		var visited = new HashSet<Integer>();
		int id;

		for (int i = 0; i < inventory.lines(); i++) {
			for (int j = 0; j < inventory.columns(); j++) {
				id = inventory.getID(i, j);

				if (id > 0 && !visited.contains(id)) {
					drawItem(graphics, inventory, i, j);
					visited.add(inventory.getID(i, j));
				}
			}
		}
	}

	public void drawTreasure(Graphics2D graphics, Treasure currentRoom) {
		var content = currentRoom.content();

		for (Integer i = 0; i < content.size(); i++) {
			var item = content.get(i);
			var coordinates = drawItem(graphics, item, 30 + 7 * width / 16 + i * 240, 10 * height / 16);
			currentRoom.addContentCoordinates(coordinates, i);
		}
	}

	public void drawBuyingRoom(Graphics2D graphics, BuyingRoom currentRoom) {
		var content = currentRoom.content();

		for (Integer i = 0; i < content.size(); i++) {
			var item = content.get(i);
			var a = drawItem(graphics, item, 30 + 7 * width / 16 + i * 240, 10 * height / 16);

			graphics.setFont(new Font("Arial", Font.ROMAN_BASELINE, 30));
			graphics.setColor(new Color(255, 209, 102));
			graphics.drawString("%d gold".formatted(item.price()), 30 + 7 * width / 16 + i * 240, 4 * height / 7);

			if (item.isInstantlyUsable()) {
				graphics.setColor(new Color(224, 86, 79));
				graphics.drawString("%d HP".formatted((((Instant) item).instantValue())), 30 + 7 * width / 16 + i * 240,
						4 * height / 7 + 35);
			}

			currentRoom.addContentCoordinates(a, i);
		}
	}

	public void drawEnemies(Graphics2D graphics, Enemies currentRoom) {
		List<Enemy> content = currentRoom.content();

		for (Integer i = 0; i < content.size(); i++) {
			var enemy = content.get(i);
			drawImage(graphics, enemy.image(), width / 2 + i * 240, 3 * height / 4 - 156, 153, 187, true);
			currentRoom.addContentCoordinates(new Rectangle(new Point(width / 2 + i * 240, 3 * height / 4 - 156),
					new Point(width / 2 + i * 240 + 153, 3 * height / 4 - 156 + 187)), i);

			// HP
			graphics.setFont(new Font("Arial", Font.PLAIN, 60));
			graphics.setColor(new Color(224, 86, 79));
			graphics.drawString("%d HP".formatted(enemy.health()), width / 2 + i * 240, 3 * height / 4 + 90);

			// DEF
			graphics.setColor(new Color(110, 114, 113));
			graphics.drawString("%d DEF".formatted(enemy.defending()), width / 2 + i * 240, 3 * height / 4 + 140);

			// Nom
			graphics.setFont(new Font("Arial", Font.PLAIN, 25));
			graphics.setColor(new Color(255, 255, 255));
			graphics.drawString("%s".formatted(enemy.name()), width / 2 + i * 240 - 20, height / 2 + 95);

			// Move
			graphics.setColor(new Color(179, 179, 179));
			if (enemy.isCasting()) {
				PassiveDamageEnemy enemyWithPassive = (PassiveDamageEnemy) enemy;
				graphics.drawString(enemyWithPassive.getEffect().formatted(enemyWithPassive.passivePower()),
						width / 2 + i * 240 - 20, height / 2 + 60);
			} else {
				graphics.drawString(
						"%s".formatted(enemy.isAttacking() ? "Attack for %s".formatted(enemy.damage())
								: "Defend for %d".formatted(enemy.nextDEF())),
						width / 2 + i * 240 - 20, height / 2 + 60);
			}
		}

		// SELECTION DE L'ENNEMI
		graphics.setColor(new Color(240, 101, 67));
		graphics.drawRect(width / 2 + currentRoom.target() * 240 - 10, 3 * height / 4 - 153, 173, 187);

		// BOUTON FINIR LE TOUR
		graphics.setColor(new Color(128, 71, 94));
		graphics.fill(new Rectangle2D.Float(width / 2 - 10, 2 * height / 5 - 5 - 67, 311, 111));
		graphics.setColor(new Color(52, 52, 74));
		graphics.fill(new Rectangle2D.Float(width / 2 - 5, 2 * height / 5 - 67, 300, 100));

		// TEXT "END TURN"
		graphics.setFont(new Font("Arial", Font.PLAIN, 50));
		graphics.setColor(new Color(255, 255, 255));
		graphics.drawString("END TURN", width / 2 - 5 + 17, 2 * height / 5);

	}

	private void drawEffects(Graphics2D graphics, Player player, Inventory inventory) {
		graphics.setFont(new Font("Arial", Font.ROMAN_BASELINE, 30));

		var itemEffects = inventory.inventoryPassiveEffects(player, false);

		for (int i = 0; i < itemEffects.size(); i++) {
			var currentEffect = itemEffects.get(i);
			drawImage(graphics, loader.image(currentEffect.effect().imageID()), width / 4 + 200 + i * 50,
					3 * height / 4 + 49, 50, 50, true);
			graphics.setColor(new Color(255, 255, 255));
			graphics.drawString("" + currentEffect.amount(), width / 4 + 200 + i * 52, 3 * height / 4 + 100);
		}

		var enemiesEffects = player.getAllEffects();

		for (int i = 0; i < enemiesEffects.size(); i++) {
			var currentEffect = enemiesEffects.get(i);
			drawImage(graphics, loader.image(currentEffect.effect().imageID()), width / 4 + 200 + i * 50,
					3 * height / 4 + 99, 50, 50, true);
			graphics.setColor(new Color(255, 255, 255));

			graphics.drawString("" + currentEffect.amount(), width / 4 + 200 + i * 52, 3 * height / 4 + 150);
		}

	}

	private void drawItemChoice(Graphics2D graphics, Item item) {
		String[] content = item.infos().split("-");

		graphics.setFont(new Font("Arial", Font.PLAIN, 30));
		graphics.setColor(Color.white);

		graphics.setColor(new Color(82, 58, 52));
		graphics.fill(new Rectangle2D.Float(width / 16 - 50, 7 * height / 16 - 10, 400, 370));

		graphics.setColor(new Color(255, 255, 255));

		for (int i = 0; i < content.length; i++) {
			graphics.drawString(content[i], 1 * width / 16, 9 * height / 16 + (i + 1) * 50 - 15);
		}
		drawImage(graphics, loader.image("rotate.png"), width / 16, 7 * height / 16, 100, 100, true);
		drawImage(graphics, loader.image("trash.png"), width / 16 + 200, 7 * height / 16, 80, 100, true);
	}

	private void drawEvent(Graphics2D graphics, GameModel data) {
		data.currentRoom().drawRoom(graphics, this);
	}

	private void draw(Graphics2D graphics, GameModel data, Player player) {

		graphics.fill(new Rectangle2D.Float(0, 0, width, height));
		drawBackground(graphics);
		drawFloor(graphics, player.currentFloor());
		drawPlayer(graphics, player);
		drawInventory(graphics, data.inventory(), player);

		if (data.currentRoom() != null) {
			drawEvent(graphics, data);
			drawEffects(graphics, player, data.inventory());
		}

		if (data.currentItemSelected() != null) {
			drawItemChoice(graphics, data.currentItemSelected());
		}
	}

	public void draw(ApplicationContext context, GameModel data, Player player) {
		context.renderFrame(graphics -> draw(graphics, data, player));
	}

	
	public void drawEnd(ApplicationContext context, String name, boolean backIsDrawn, int score) {
		context.renderFrame(graphics -> drawEnd(graphics, name, backIsDrawn, score));
	}

	private void drawEnd(Graphics2D graphics, String name, boolean backIsDrawn, int score) {
		drawBackground(graphics);

		graphics.setFont(new Font("Arial", Font.ROMAN_BASELINE, 35));
		graphics.setColor(Color.white);
		graphics.drawString("Congratulations, You won with a score of %d".formatted(score), 0, height / 5);
		graphics.drawString("Write your name to sign in the ranking !", 0, height / 4);
		graphics.drawString("Use Space to confirm", 0, height / 3);

		graphics.drawString(name, 0, height / 2);

		if (!backIsDrawn) {
			try (var reader = Files.newBufferedReader(Path.of("scores.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					top.put(extractScore(line), line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		drawPodium(graphics, top);
	}

	private int extractScore(String line) {
		int i = 0;
		while (line.charAt(i) != ':') {
			i++;
		}
		return Integer.parseInt(line.substring(0, i));
	}
	
	private void drawPodium(Graphics2D graphics, TreeMap<Integer,String> podium) {
		int i = 0;
		graphics.drawString("Podium", width / 2, height / 3 -60);
		for (var player : podium.descendingKeySet() ) {
			if (i > 2) {
				return;
			}
			graphics.drawString((i+1)+" | "+podium.get(player), width / 2, height / 3 + i++*60);
		}
	}
}