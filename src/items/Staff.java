package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Staff implements Weapon, Magic {
	private final String name;
	private final int rarity;
	private ItemShape shape;
	private final int id;
	private BufferedImage image;

	private int price = 0;
	private final int damagePoints;
	private final int manaCost;
	private final int energyCost;

	public Staff(String name, ItemShape shape, int rarity, int damagePoints, int manaCost, int energyCost,
			BufferedImage image) {
		this.name = Objects.requireNonNull(name);
		this.shape = Objects.requireNonNull(shape);
		this.image = Objects.requireNonNull(image);
		this.rarity = rarity;
		this.id = generateID();
		this.damagePoints = damagePoints;
		this.manaCost = manaCost;
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
		return "%s-%s-Deals %d DMG-Costs %d mana -and %s energy".formatted(name, getRarityName(), damagePoints, manaCost, energyCost);
	}

	@Override
	public int manaCost() {
		return manaCost;
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
		if (inventory.enoughRessources("Mana",manaCost) && data.useEnergy(player, this) && inventory.useQuantifiable("Mana")) {
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
	public Staff copy() {
		return new Staff(name, shape, rarity, damagePoints, manaCost, energyCost, image);
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Staff o && (name == o.name());
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
