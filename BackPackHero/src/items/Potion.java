package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Potion implements Consumable {
	private final String name;
	private final int rarity;
	private ItemShape shape;
	private final int id;
	private BufferedImage image;
	private int price = 0;

	private final int restorePoints;
	private final boolean isHealing;

	public Potion(String name, ItemShape shape, int rarity, int restorePoints, boolean isHealing, BufferedImage image) {
		this.shape = Objects.requireNonNull(shape);
		this.name = Objects.requireNonNull(name);
		this.image = Objects.requireNonNull(image);
		this.rarity = rarity;
		this.id = generateID();
		this.restorePoints = restorePoints;
		this.isHealing = isHealing;

	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public ItemShape shape() {
		return shape;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int rarity() {
		return rarity;
	}

	@Override
	public BufferedImage image() {
		return image;
	}

	@Override
	public String toString() {
		return "  " + id + " ";

	}

	@Override
	public String infos() {
		return "%s-%s-Restore %d %s".formatted(name, getRarityName(), restorePoints, (isHealing ? "HP" : "stamina"));
	}

	@Override
	public void setShape(ItemShape shape) {
		this.shape = Objects.requireNonNull(shape);
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image = Objects.requireNonNull(image);
	}

	@Override
	public Potion copy() {
		return new Potion(name, shape, rarity, restorePoints, isHealing, image);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Potion o && (name == o.name());
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {

		if (isHealing) {
			player.increaseHP(restorePoints);
		} else {
			player.increaseStamina(restorePoints);
		}
		inventory.removeFromInventory(id);
		return true;

	}

	@Override
	public int price() {
		return price;
	}

	@Override
	public int setPrice() {
		this.price = Generators.randint((rarity + 1) * 4, (rarity + 2) * 4);
		return price;
	}

}
