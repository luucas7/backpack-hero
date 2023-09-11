package items;

import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class InstantHeal implements Miscellaneous, Instant {
	private final ItemShape shape;
	private final int id;
	private BufferedImage image;
	private int price = 0;
	private int heal;

	public InstantHeal(ItemShape shape, int heal, BufferedImage image) {
		this.shape = Objects.requireNonNull(shape);
		this.image = Objects.requireNonNull(image);
		this.heal = heal;
		this.id = generateID();
		setPrice();
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
	public BufferedImage image() {
		return image;
	}
	
	@Override
	public int instantValue() {
		return heal;
	}

	@Override
	public String toString() {
		return "  " + id + " ";
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image = Objects.requireNonNull(image);
	}

	public InstantHeal copy() {
		return new InstantHeal(shape, Generators.randint(5, 20),image);
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		if (inventory.enoughRessources(price)) {
			player.increaseHP(heal);
		}
		return true;
	}

	@Override
	public int price() {
		return price;
	}

	@Override
	public int setPrice() {
		this.price = Generators.randint(heal - 3, heal + 4);
		return price;
	}

	@Override
	public String name() {
		return "Heal";
	}

	@Override
	public int rarity() {
		return 0;
	}

	@Override
	public void setShape(ItemShape shape) {

	}

	@Override
	public String infos() {
		return "";
	}

}
