package utility;

import java.util.Random;

public class Generators {
	private Generators() {
	}
	
	private static int id = 0;
	private static Random randomGenerator = new Random();

	public static int generateID() {
		return ++id;
	}

	public static int randint(int right, int left) {
		return randomGenerator.nextInt(right,left+1);
	}

	public static int randint(int left) {
		return randomGenerator.nextInt(left+1);
	}

	public static boolean nextBool() {
		return randomGenerator.nextBoolean();
	}

	public static boolean nextBool(int falseFactor, int trueFactor) {
		int res = randint(1, trueFactor + falseFactor);
		return res <= falseFactor;
	}
}
