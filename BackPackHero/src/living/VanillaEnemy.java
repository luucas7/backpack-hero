package living;

import java.awt.image.BufferedImage;
import java.util.Objects;
import utility.Generators;

public class VanillaEnemy implements Enemy {
	private final String name;
	private int health;
	private final int damage;
	private final int id;
	private final BufferedImage image;
	private boolean isAttacking;
	private int defending = 0;
	private boolean isDefending = false;
	private int nextDEF;

	public VanillaEnemy(String name, int health, int damage, BufferedImage image) {
		this.name = Objects.requireNonNull(name);
		this.image = Objects.requireNonNull(image);
		this.health = health;
		this.damage = damage;
		this.id = Generators.generateID();
		setAction();
	}

	public String name() {
		return name;
	}

	public int health() {
		return health;
	}

	public int damage() {
		return damage;
	}

	public BufferedImage image() {
		return image;
	}

	@Override
	public void decreaseHP(int hp) {

		if (hp >= defending) {
			health = health - (hp - defending);
			defending = 0;
		} else {
			defending -= hp;
		}
	}

	public int defending() {
		return defending;
	}

	public boolean isAttacking() {
		return isAttacking;
	}

	public int nextDEF() {
		return nextDEF;
	}

	@Override
	public void increaseHP(int hp) {
		this.health += hp;
	}

	@Override
	public void decreaseDEF(int def) {
		this.defending -= def;
	}

	@Override
	public void increaseDEF(int def) {
		this.defending += def;
	}

	@Override
	public void setDEF(int def) {
		this.defending = def;
	}

	@Override
	public int hashCode() {
		return Objects.hash(health, name, defending, id);
	}

	public boolean equals(Object other) {
		return other instanceof Enemy o && o.name().equals(name) && o.health() == health; // IDK if id or not
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public VanillaEnemy copy() {
		return new VanillaEnemy(name, health, damage, image);
	}

	public void setAction() {
		isAttacking = Generators.nextBool(3, 5);
		if (!isAttacking) {
			isDefending = true;
			nextDEF = Generators.randint(health / 5, health / 4);
		}
	}

	@Override
	public void doAction(Player player) {
		if (isAttacking)
			player.decreaseHP(damage);

		if (isDefending) {
			setDEF(nextDEF);
			isDefending = false;
		}
		setAction();
	}

	@Override
	public void setDefendingForNextTurn() {
		this.isDefending = true;
	}

}
