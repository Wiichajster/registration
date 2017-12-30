package util;

import java.security.NoSuchAlgorithmException;

import model.User;

public class AuthHelper {
	public static boolean validate(User user, String password) throws NoSuchAlgorithmException {
		boolean result = false;
		if (user == null || password == null || password.equals("")) {
			return result;
		}
		String hashedPass = HashingHelper.hashString(password);
		if (hashedPass.equals(user.getPassword())) {
			result = true;
		}

		return result;
	}
}
