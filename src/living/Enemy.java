package living;

import java.awt.image.BufferedImage;
import utility.Generators;

public interface Enemy extends Living {

	int id();

	String name();
	void setAction();

	BufferedImage image();

	default int generateID() {
		return Generators.generateID();
	}

	Enemy copy();

	boolean equals(Object other);

	int damage();

	boolean isAttacking();
	
	int defending();
	int nextDEF();
	
	void setDefendingForNextTurn();
	
	void doAction(Player player);
	
	
	default boolean canDoPassiveDamage() {
		return false;
	}

	default boolean isCasting() {
		return false;
	}

}
