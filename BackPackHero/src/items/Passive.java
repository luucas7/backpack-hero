package items;

import living.PassiveEffect;
import living.Player;

public interface Passive extends Item {

	@Override
	default boolean isPassive() {
		return true;
	}
	
	PassiveEffect effect();

	void activatePassive(Player player);

}
