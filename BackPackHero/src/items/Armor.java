package items;
import java.awt.image.BufferedImage;
import java.util.Objects;

import game.GameModel;
import game.Inventory;
import living.Effect;
import living.PassiveEffect;
import living.Player;
import utility.Generators;

public class Armor implements Item,Miscellaneous,Passive {
	private final String name ;
	private final int rarity;
	private ItemShape shape;
	private final int id;
	private BufferedImage image;
	
	private int price = 0;
	private final int protectionPoints;
	private final Effect effect;
	
	public Armor(String name, ItemShape shape, int rarity, int protectionPoints, Effect effect, BufferedImage image) {
		this.name = Objects.requireNonNull(name);
		this.shape = Objects.requireNonNull(shape);
		this.image = Objects.requireNonNull(image);
		
		this.rarity = rarity;
		this.id = generateID();
		this.protectionPoints = protectionPoints;
		this.effect = effect;
		
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
		return "%s-%s-Gives %d DEF".formatted(name,getRarityName(),protectionPoints);
	}

	@Override
	public int rarity() {
		return rarity;
	}
	
	@Override
	public BufferedImage image() {
		return image;
	}
	
	public int protectionPoints() {
		return protectionPoints;
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
	public Armor copy() {
		return new Armor(name, shape, rarity, protectionPoints, effect, image);
	}

	@Override
	public boolean doAction(GameModel data, Inventory inventory, Player player) {
		return false;
	}
	
	@Override
	public void activatePassive(Player player) {
		player.increaseDEF(protectionPoints);
	}
	
	@Override
	public boolean equals (Object other) {
		return other instanceof Armor o && (name == o.name());
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
	
	public PassiveEffect effect() {
		return new PassiveEffect(effect,protectionPoints);
	}

	
	
}
