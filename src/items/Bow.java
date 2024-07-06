package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Bow implements Weapon {
	private final String name;
	private final int rarity;
	private ItemShape shape;
	private final int id;
	private BufferedImage image;

	private int price = 0;
	private final int damagePoints;
	private final int energyCost;

	public Bow(String name, ItemShape shape, int rarity, int damagePoints, int energyCost,
			BufferedImage image) {
		this.name = Objects.requireNonNull(name);
		this.shape = Objects.requireNonNull(shape);
		this.image = Objects.requireNonNull(image);
		this.rarity = rarity;
		this.id = generateID();
		this.damagePoints = damagePoints;
		this.energyCost = energyCost;
		
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
		return "%s-%s-Deals %d DMG-Uses %d energy".formatted(name, getRarityName(), damagePoints, energyCost);
	}

	@Override
	public int energyCost() {
		return energyCost;
	}

	@Override
	public int damagePoints() {
		return damagePoints;
	}

	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		if (data.hasEnoughEnergy(player,energyCost) && inventory.useQuantifiable("Arrow") && (data.useEnergy(player, this))) {
		return data.doDamage(player, this);
		}
		return false;
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
	public Bow copy() {
		return new Bow(name, shape, rarity, damagePoints, energyCost, image);
	}
	
	@Override
	public boolean equals (Object other) {
		return other instanceof Bow o && (name == o.name());
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
