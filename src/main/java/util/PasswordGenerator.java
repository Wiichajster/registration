package util;

import java.util.Random;

public class PasswordGenerator {
	public static String generate(int length) throws IllegalArgumentException {
		if (length <= 1)
			throw new IllegalArgumentException();
		StringBuilder builder = new StringBuilder();
		Random generator = new Random();

		String allowedSymbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i = 0; i < length; i++) {
			builder.append(allowedSymbols.charAt(generator.nextInt(allowedSymbols.length())));
		}

		return builder.toString();
	}
}
