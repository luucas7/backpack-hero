package items;

public interface Instant extends Miscellaneous{
	
	
	@Override
	default boolean isInstantlyUsable() {
		return true;
	}

	int instantValue();
}
