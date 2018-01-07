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

	public static String hashString(String text) {
		String result = null;

		if (text == null) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());
			result = new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
