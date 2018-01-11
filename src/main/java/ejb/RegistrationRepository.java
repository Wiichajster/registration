package ejb;

import javax.mail.MessagingException;

import model.User;
import model.VerificationToken;

public interface RegistrationRepository {
	public boolean add(User user) throws MessagingException;

	public boolean sendActivationEmail(VerificationToken token) throws MessagingException;
	
	public boolean checkLoginUsage(String username) throws IllegalArgumentException;
}
