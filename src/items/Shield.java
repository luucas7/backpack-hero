package items;
import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Player;
import utility.Generators;

public class Shield implements Item {
	private final String name ;
	private final int rarity;
	private ItemShape shape;
	private final int id;
	private BufferedImage image;
	
	private int price = 0;
	private final int protectionPoints;
	private final int energyCost;
	
	public Shield(String name, ItemShape shape, int rarity, int protectionPoints, int energyCost, BufferedImage image) {
		this.name = Objects.requireNonNull(name);
		this.shape = Objects.requireNonNull(shape);
		this.image = Objects.requireNonNull(image);
		
		this.rarity = rarity;
		this.id = generateID();
		this.protectionPoints = protectionPoints;
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
	public String toString() {
		return "  "+id+" ";
		
	}
	
	public String infos() {
		return "%s-%s-Gives %d DEF-Uses %d energy".formatted(name,getRarityName(),protectionPoints,energyCost);
	}

	@Override
	public int rarity() {
		return rarity;
	}
	
	@Override
	public BufferedImage image() {
		return image;
	}
	
	public int energyCost() {
		return energyCost;
	}
	
	public int protectionPoints() {
		return protectionPoints;
	}
	
	// mhh
	public boolean isUsable(int playerEnergy) {
		return energyCost() <= playerEnergy;
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
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		return data.doProtect(player, this);
	}
	
	@Override
	public Shield copy() {
		return new Shield(name, shape, rarity, protectionPoints, energyCost, image);
	}
	
	@Override
	public boolean equals (Object other) {
		return other instanceof Shield o && (name == o.name());
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
