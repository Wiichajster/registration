package ejb;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.User;
import model.VerificationToken;
import util.HashingHelper;
import util.MessageSender;
import util.PasswordGenerator;

@Stateless
public class AccountRepositoryImpl implements AccountRepository {

	// pola prywatne

	@PersistenceContext
	private EntityManager em;

	@Inject
	private MessageSender messageSender;

	// metody publiczne

	/*
	 * Metoda pobierająca z bazy danych obiek użytkownika po jego nazwie
	 */
	@Override
	public User get(String username) {
		return em.find(User.class, username);
	}

	/*
	 * Metoda pobierająca z bazy danych token weryfikacyjny porzez skojarzony z nim
	 * hashCode
	 */
	@Override
	public VerificationToken getByHash(String hash) {
		if (hash == null || "".equals(hash)) {
			return null;
		}

		TypedQuery<VerificationToken> query = em.createNamedQuery("VerificationToken.FindByHash",
				VerificationToken.class);
		query.setParameter("tokenHash", hash);
		VerificationToken token = query.getSingleResult();

		return token;
	}

	/*
	 * Metoda resetująca hasło użytkownika. Kojarzenie użytkownika odbywa się
	 * poprzez przypisany do niego adres email. Następnie generowane jest nowe,
	 * losowe hasło, które następnie przypisywane jest do użykownika. W celu
	 * dostarczenia użytkownikowi nowego hasła wysyłana jest do użytkownika
	 * wiadomość email.
	 */
	@Override
	public boolean restartPassword(String email) throws MessagingException {
		if (email == null || "".equals(email)) {
			return false;
		}

		boolean result = false;
		String newPassword = PasswordGenerator.generate(8);
		String hashedPassword = HashingHelper.hashString(newPassword);

		TypedQuery<User> query = em.createNamedQuery("User.FindByMail", User.class);
		query.setParameter("email", email);
		User user = query.getSingleResult();
		if (user != null) {
			user.setPassword(hashedPassword);
			em.merge(user);
			sendNewPasswordEmail(user, newPassword);
			result = true;
		}

		return result;

	}

	/*
	 * Metoda służąca do aktywacji użytkownika. W pierwszej kolejności pobierany
	 * jest z bazy danych VerificationToken, a następnie powiązany z tokenem obiekt
	 * użytkownika. Pole isActive używkownika jest ustawiane na true, a następnie
	 * tak zmodyfikowany obiekt zapisywany jest w bazie danych. VerificationToken po
	 * aktywacji konta jest usuwany z bazy danych.
	 */
	@Override
	public boolean activateUser(String hash) {
		if (hash == null || "".equals(hash)) {
			return false;
		}
		
		boolean result = false;

		VerificationToken token = getByHash(hash);

		if (token != null && token.getExpirationDateTime().compareTo(token.getRegistrationDateTime()) >= 0) {
			User user = token.getUser();
			user.setActive(true);
			em.merge(user);
			em.remove(token);
			result = true;
		}
		return result;
	}

	// metody prywatne

	/*
	 * Metoda służąca do wysyłania wiadomości email z nowym hasłem na adres
	 * użytkownika podany przy rejestracji.
	 */
	private void sendNewPasswordEmail(User user, String newPassword) throws MessagingException {
		String recipient = user.getEmail();
		String subject = "Nowe hasło";
		String message = createPasswordGenerationMessage(newPassword);

		messageSender.sendHtmlEmail(recipient, subject, message);
	}

	/*
	 * Metoda generująca wiadomość z nowym hasłem która następnie wysyłana jest do
	 * użytkownika
	 */

	private String createPasswordGenerationMessage(String newPassword) {
		if (newPassword == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();

		builder.append("<h2>Witaj!</h2><br/><br/>");
		builder.append("<p>Twoje nowe hasło: </p>");
		builder.append(newPassword);
		builder.append("<br/><p>Jeżeli to nie ty, zignoruj tą wiadomość</p>");
		return builder.toString();
	}

}
