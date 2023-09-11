package living;


public record PassiveEffect(Effect effect, int amount) {

	public void applyEffect(Player player) {
		switch (effect) {
		case REGEN: player.increaseHP(amount); break;
		case POISON: player.decreaseHPthroughDEF(amount) ; break;
		case BURN: player.decreaseHP(amount); break;
		case BLOCK: player.increaseDEF(amount); break;
		default: break;
		}
	}
}
