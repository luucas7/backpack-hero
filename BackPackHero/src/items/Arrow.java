package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Arrow implements Weapon, Quantifiable {
	private final String name;
	private final int rarity;
	private ItemShape shape;
	private final int id;
	private BufferedImage image;
	private int count;

	private int price = 0;
	private final int damagePoints;
	private final int energyCost;

	public Arrow(String name, ItemShape shape, int rarity, int count, int damagePoints, BufferedImage image) {
		this.shape = Objects.requireNonNull(shape);
		this.name = Objects.requireNonNull(name);
		this.image = Objects.requireNonNull(image);
		this.rarity = rarity;
		this.id = generateID();
		this.count = count;
		this.damagePoints = damagePoints;
		this.energyCost = 0;
		
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
		return "%s-%s-Throw for %d DMG".formatted(name, getRarityName(), damagePoints);
	}

	@Override
	public int energyCost() {
		return energyCost;
	}

	@Override
	public int damagePoints() {
		return damagePoints;
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		if (useOne(inventory)) {
			return (data.doDamage(player, this));
		}
		return false;
	}

	@Override
	public void dropOne() {
		count--;
	}

	@Override
	public void setShape(ItemShape shape) {
		this.shape = Objects.requireNonNull(shape);
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image = Objects.requireNonNull(image);
	}

	public Arrow copy() {
		return copy(Generators.randint(5,7));
	}
	
	public Arrow copy(int count) {
		return new Arrow(name, shape, rarity, count, damagePoints, image);
	}
	
	@Override
	public boolean equals (Object other) {
		return other instanceof Arrow o && (name == o.name());
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
	this.price = Generators.randint((rarity+1)*4,(rarity+2)*4 );
		return price;
	}
}
