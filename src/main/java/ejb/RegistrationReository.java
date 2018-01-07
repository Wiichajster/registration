package ejb;

import javax.mail.MessagingException;
import javax.validation.ConstraintViolationException;

import exceptions.UserAlreadyExistException;
import model.User;
import model.VerificationToken;

public interface RegistrationReository {
	public boolean add(User user) throws ConstraintViolationException, MessagingException, UserAlreadyExistException;

	public boolean sendActivationEmail(VerificationToken token) throws MessagingException;
}
