package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Klasa pomocnicza do kodowania hasła przy pomocy algorytmu hashującego MD5
 * 
 * @author Damian Dalecki
 *
 */

public class HashingHelper {

	public static String hashString(String password) throws NoSuchAlgorithmException {
		String result = null;

		if (password == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			result = new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException ex) {
			throw new NoSuchAlgorithmException("Błąd podczas kodowania hasła", ex);
		}
		return result;
	}
}
