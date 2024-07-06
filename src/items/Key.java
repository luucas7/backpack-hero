package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Key implements Miscellaneous {
	private final int id;
	private BufferedImage image;
	private int price = 0;

	private static ItemShape staticShape;
	private static BufferedImage staticImage;

	public Key() {
		this.image = staticImage;
		this.id = generateID();
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public ItemShape shape() {
		return staticShape;
	}

	@Override
	public String name() {
		return "Key";
	}

	@Override
	public int rarity() {
		return 0;
	}

	@Override
	public BufferedImage image() {
		return image;
	}

	@Override
	public String toString() {
		return "  " + id + " ";

	}

	public String infos() {
		return "%s-%s".formatted(name(), getRarityName());
	}

	@Override
	public void setShape(ItemShape shape) {
		// Shape will always be the same
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image = Objects.requireNonNull(image);
	}

	@Override
	public Key copy() {
		return new Key();
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		return false;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Key o && (name() == o.name());
	}

	@Override
	public int price() {
		return price;
	}

	@Override
	public int setPrice() {
		this.price = Generators.randint(4, 8);
		return price;
	}
	
	public static Key create() {
		return new Key();
	}
	
	public static void initiateStaticKey(ItemShape shape, BufferedImage image) {
		staticShape = shape;
		staticImage = image;
	}
}
