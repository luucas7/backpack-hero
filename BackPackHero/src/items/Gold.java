package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Gold implements Miscellaneous, Quantifiable {
	private final int id;
	private BufferedImage image;
	private int count;
	private int price = 0;
	
	private static ItemShape staticShape;
	private static BufferedImage staticImage;

	public Gold(int count) {
		this.image = staticImage;
		this.id = generateID();
		this.count = count;
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
		return "Gold";
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
		return "%s".formatted("Gold");
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
	public Gold copy() {
		return copy(Generators.randint(5, 7));
	}
	
	public Gold copy(int count) {
		return new Gold(count);
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Gold o && (name() == o.name());
	}
	
	@Override
	public void dropOne() {
		count--;
	}

	@Override
	public int count() {
		return count;
	}

	@Override
	public int addToCount(int toAdd) {
		count += toAdd;
		return count;
	}
	
	@Override
	public int price() {
		return price;
	}
	
	@Override
	public int setPrice() {
	this.price = Generators.randint(4,8);
		return price;
	}
	
	public static Gold create() {
		return new Gold(Generators.randint(4,8));
	}
	
	public static Gold initiateStaticGold(ItemShape shape, BufferedImage image) {
		staticShape = shape;
		staticImage = image;
		return create();
	}
	
}
