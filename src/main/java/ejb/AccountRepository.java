package ejb;

import javax.mail.MessagingException;

import model.User;
import model.VerificationToken;

public interface AccountRepository {

	public User get(String username);

	public VerificationToken getByHash(String hash);

	public boolean restartPassword(String email) throws MessagingException;

	public boolean activateUser(String hash);
}
