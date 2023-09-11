package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Mana implements Miscellaneous, Quantifiable {
	private final String name = "Mana orb";
	private final ItemShape shape;
	private final int id;
	private BufferedImage image;
	private int count;
	private int price = 0;

	public Mana(ItemShape shape, int count, BufferedImage image) {
		this.shape = Objects.requireNonNull(shape);
		this.image = Objects.requireNonNull(image);
		this.id = generateID();
		this.count = count;
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
	public BufferedImage image() {
		return image;
	}

	@Override
	public String toString() {
		return "  " + id + " ";

	}

	public String infos() {
		return "%s-%s".formatted(name, getRarityName());
	}

	@Override
	public void setShape(ItemShape shape) {
		// Shape will always be the same
	}

	@Override
	public void dropOne() {
		count--;
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image = Objects.requireNonNull(image);
	}

	@Override
	public Mana copy() {
		return new Mana(shape, count, image);
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		return false;
	}

	@Override
	public int rarity() {
		return 0;
	}
	
	@Override
	public boolean equals (Object other) {
		return other instanceof Mana o && (name == o.name());
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
	this.price = Generators.randint((rarity()+1)*4,(rarity()+2)*4 );
		return price;
	}

}
