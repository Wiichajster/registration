package ejb;

import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import exceptions.UserAlreadyExistException;
import model.User;
import model.VerificationToken;
import util.MessageSender;

/**
 * 
 * Repozytorium metod wykorzystywanych w procesie rejestracji użytkownika.
 * 
 * @author Damian Dalecki
 *
 */

@Stateless
public class RegistrationReositoryImpl implements RegistrationReository {

	// pola prywatne

	@PersistenceContext
	private EntityManager em;

	@Inject
	private MessageSender messageSender;
	
	@Resource
	private Validator validator;

	// metody publiczne

	/*
	 * Metoda służąca do dodania użtkownika i utrwalenia go w bazie danych. Przy
	 * rejestracji konto użytkonika jest nieaktywne. Tworzony jest powiązany z nim
	 * VerificationToken, czyli dodatkowy obiekt pomocniczy do aktywacji konta
	 */
	public boolean add(User user) throws ConstraintViolationException, MessagingException, UserAlreadyExistException {
		if (user == null) {
			return false;
		}

		boolean result = false;
		VerificationToken token = new VerificationToken();

		// jeżeli nazwa zajęta wyrzuć wyjątek
		if (checkLoginUsage(user.getUsername())) {
			throw new UserAlreadyExistException("Nazwa jest już zajęta");
		}

		user.getRoles().add("user");
		token.setUser(user);
		token.setTokenHash();
		try {
			validator.validate(user);
			em.persist(user);
			em.persist(token);

			sendActivationEmail(token);
			result = true;
		} catch (ConstraintViolationException ex) {
			System.out.println("RegistrationRepository: Constraint violation");
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for(ConstraintViolation<?> v: violations) {
				System.out.println(v.getMessage());
			}
			
			//throw new ConstraintViolationException(ex.getConstraintViolations());
		} catch (MessagingException ex) {
			throw ex;
		}

		return result;
	}

	/*
	 * Metoda wysyłająca emaila na konto podane przy rejestracji z linkiem
	 * aktywacyjnym
	 */
	@Override
	public boolean sendActivationEmail(VerificationToken token) throws MessagingException {
		boolean result = false;

		String recipient = token.getUser().getEmail().toString();
		String subject = "Wiadomość aktywacyjna";
		String message = createActivationMessage(token);

		try {
			messageSender.sendHtmlEmail(recipient, subject, message);
			result = true;
		} catch (MessagingException ex) {
			throw ex;
		}

		return result;
	}

	// metody prywatne

	/*
	 * Metoda do sprawdzenia czy w bazie danych istnieje już użytkownik o podanej
	 * nazwie
	 */

	private boolean checkLoginUsage(String username) {
		boolean result = false;

		User user = em.find(User.class, username);

		if (user != null) {
			result = true;
		}

		return result;
	}

	/*
	 * Metoda tworząca na podstawie dostarczonego obiektu VerificationToken
	 * wiadomość aktywacyjną
	 * 
	 * @return zwraca łańcuch typu String zawierający skonstruowaną wiadomość
	 */

	private String createActivationMessage(VerificationToken token) {
		StringBuilder builder = new StringBuilder();

		builder.append("<h2>Witaj " + token.getUser().getUsername() + "!</h2><br/><br/>");
		builder.append("<p>W celu aktywacji konta kliknij na poniższy link: </p><br/>");
		builder.append("<a href=\"http://localhost:8080/registration/activate?hash=" + token.getTokenHash()
				+ "\">Klik</a><br/>");
		builder.append("<p>Jeżeli to nie ty zakładałeś to konto to zignoruj tą wiadomość</p>");

		return builder.toString();
	}

}
