package living;

public interface Living {

	int health();
	int defending();
	
	default boolean isDead() {
		return health() <= 0;
	}
	
	void decreaseHP(int i);
	void increaseHP(int i);
	void decreaseDEF(int i);
	void increaseDEF(int i);
	void setDEF(int i);
	


}
