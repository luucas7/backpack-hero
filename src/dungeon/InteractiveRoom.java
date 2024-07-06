package dungeon;

public interface InteractiveRoom extends Room {

	@Override
	default boolean hasAnInteraction() {
		return true;
	}

}
