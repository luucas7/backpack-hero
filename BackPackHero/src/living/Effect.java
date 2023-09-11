package living;

import java.util.Objects;

public enum Effect {
	REGEN("Regen.png"),
	POISON("Poison.png"),
	BURN("Burn.png"),
	BLOCK("Block.png");
	
	private final String imageID;
	
	
	private Effect(String imageID) {
		this.imageID = Objects.requireNonNull(imageID);
	}
	
	public String imageID() {
		return imageID;
	}
	
	@Override
	public String toString() {
		switch(this) {
		case REGEN: return "Regen for %d HP";
		case BLOCK: return "Block for %d HP";
		case BURN: return "Burn for %d HP";
		case POISON: return "Poison for %d HP";
		default: return "huh";
		}
	}
}
