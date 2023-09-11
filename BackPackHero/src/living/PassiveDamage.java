package living;

public interface PassiveDamage extends Enemy {

	@Override
	default boolean canDoPassiveDamage() {
		return true;
	}

	int passivePower();

	boolean didCast();
	@Override
	default void doAction(Player player) {
		if (isAttacking())
			player.decreaseHP(damage());
		if (didCast())
			player.decreaseHP(passivePower());
		setAction();

	}
	
}
