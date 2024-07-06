package items;

import java.awt.image.BufferedImage;
import java.util.List;

import dungeon.ContentRoom;
import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public interface Item {
	static final List<String> rarities = List.of("Common", "Uncommon", "Rare", "Epic", "Legendary");

	ItemShape shape();

	int id();

	String name();

	int rarity();

	void setShape(ItemShape shape);

	void setImage(BufferedImage image);

	default boolean isQuantifiable() {
		return false;
	}
	
	default boolean isInstantlyUsable() {
		return false;
	}

	default String getRarityName() {
		return rarities.get(rarity());
	}

	default boolean isUsableInFight() {
		return true;
	}

	default boolean isPassive() {
		return false;
	}
	
	default boolean isConsumable() {
		return true;
	}

	default void rotateItem(Inventory inventory) {

		if (inventory.isRotatingPossible(id())) {
			setImage(rotateImage(image()));
		}
	}

	default void rotateItem(ContentRoom room) {
		System.out.println("Rotation d'un item hors inventaire");
		setNewShape();
		setImage(rotateImage(image()));
	}

	default void setNewShape() {
		setShape(shape().rotateShape());
	}

	boolean doAction(GameModel data, Inventory inventory, Player player);

	BufferedImage image();

	String infos();

	default BufferedImage rotateImage(BufferedImage image) {

		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, image.getType());

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				newImage.setRGB(height - 1 - j, i, image.getRGB(i, j));

		return newImage;
	}

	default int generateID() {
		return Generators.generateID();
	}

	Item copy();

	boolean equals(Object other);

	default int price() {
		return 0;
	}

	int setPrice();

}
