package ejb;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;

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
public class RegistrationRepositoryImpl implements RegistrationRepository {

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
	public boolean add(User user) throws MessagingException {
		if (user == null) {
			return false;
		}

		boolean result = false;
		VerificationToken token = new VerificationToken();

		/*
		 * // jeżeli nazwa zajęta wyrzuć wyjątek if
		 * (checkLoginUsage(user.getUsername())) { throw new
		 * UserAlreadyExistException("Nazwa jest już zajęta"); }
		 */

		user.getRoles().add("user");
		token.setUser(user);
		token.setTokenHash();
		getValidator().validate(user);
		em.persist(user);
		em.persist(token);

		if (sendActivationEmail(token))
			result = true;

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
			getMessageSender().sendHtmlEmail(recipient, subject, message);
			result = true;
		} catch (MessagingException ex) {
			throw ex;
		}

		return result;
	}

	/*
	 * Metoda do sprawdzenia czy w bazie danych istnieje już użytkownik o podanej
	 * nazwie
	 */

	public boolean checkLoginUsage(String username) throws IllegalArgumentException {
		boolean result = false;

		if (username == null || "".equals(username)) {
			throw new IllegalArgumentException();
		}
		User user = em.find(User.class, username);

		if (user != null) {
			result = true;
		}

		return result;
	}

	// metody prywatne

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

	// gettery i settery

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
