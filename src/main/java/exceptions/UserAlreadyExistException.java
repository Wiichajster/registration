package exceptions;

public class UserAlreadyExistException extends Exception {

	private static final long serialVersionUID = -8598539968360469050L;

	public UserAlreadyExistException(String message) {
		super(message);
	}
}
