package living;

import java.awt.image.BufferedImage;
import java.util.Objects;
import utility.Generators;

public class PassiveDamageEnemy implements PassiveDamage {
	private final String name;
	private int health;
	private final int damage;
	private final int id;
	private final BufferedImage image;
	private int defending = 0;
	private boolean isAttacking;
	private boolean isDefending = false;
	private int nextDEF;
	private boolean isCasting = false;
	private boolean didCast = false;
	private final int passivePower;
	private final Effect effect;

	public PassiveDamageEnemy(String name, int health, int damage, int passivePower, Effect effect, BufferedImage image) {
		this.name = Objects.requireNonNull(name);
		this.image = Objects.requireNonNull(image);
		this.effect = effect;
		this.health = health;
		this.damage = damage;
		this.passivePower = passivePower;
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

	public int nextDEF() {
		return nextDEF;
	}

	public int passivePower() {
		return passivePower;
	}

	public BufferedImage image() {
		return image;
	}

	@Override
	public int defending() {
		return defending;
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

	public boolean isAttacking() {
		return isAttacking;
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
	public PassiveDamageEnemy copy() {
		return new PassiveDamageEnemy(name, health, damage, passivePower, effect, image);
	}

	@Override
	public boolean didCast() {
		return didCast;
	}


	public void setAction() {
		
		if (didCast) {
			isAttacking = Generators.nextBool(3, 5);
			if (!isAttacking) {
				isDefending = true;
				nextDEF = Generators.randint(health / 5, health / 4);
			}
		}
		else {
			this.isCasting = true;
			didCast = true;
		}
	}

	@Override
	public void doAction(Player player) {
		if (isAttacking)
			player.decreaseHP(damage);

		else if (isDefending) {
			setDEF(nextDEF);
			isDefending = false;
		}
		else {
			didCast = true;
			this.isCasting = false;
			player.addEffect(-id,new PassiveEffect(effect,passivePower));
		}

		setAction();
	}

	@Override
	public void setDefendingForNextTurn() {
		this.isDefending = true;
	}
	
	@Override
	public boolean isCasting() {
		return isCasting;
	}
	
	public String getEffect() {
		return effect.toString();
	}

}
