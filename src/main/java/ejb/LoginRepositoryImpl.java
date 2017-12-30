package ejb;

import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import exceptions.UserAlreadyExistException;
import model.User;
import model.VerificationToken;

@Stateless
public class LoginRepositoryImpl implements LoginRepository {

	@PersistenceContext
	private EntityManager em;

	public boolean add(User user) throws EJBException, UserAlreadyExistException {
		boolean result = false;
		VerificationToken token = null;

		if (checkLoginUsage(user.getUsername())) {
			throw new UserAlreadyExistException("Nazwa jest już zajęta");
		}

		try {
			user.getRoles().add("user");
			token = new VerificationToken();
			token.setUser(user);
			token.setTokenHash();
			em.persist(user);
			em.persist(token);
			sendActivationEmail(token);
			result = true;
		} catch (Exception ex) {
			throw new EJBException("Błąd rejestracji użytkownika", ex);
		}
		return result;
	}

	public User get(String username) {
		return em.find(User.class, username);
	}

	private boolean checkLoginUsage(String username) {
		boolean result = false;

		User user = get(username);

		if (user != null) {
			result = true;
		}

		return result;
	}

	private void sendActivationEmail(VerificationToken token) throws MessagingException {

		String username = "ddalecki88@gmail.com";
		String password = "!!Dam1988!!";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Message message = new MimeMessage(session);

		message.setFrom(new InternetAddress(username));

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(token.getUser().getEmail()));

		message.setSubject("Wiadomość aktywacyjna");

		message.setContent(createActivationMessage(token), "text/html");

		Transport.send(message);

	}

	private String createActivationMessage(VerificationToken token) {
		StringBuilder builder = new StringBuilder();

		builder.append("<h2>Witaj " + token.getUser().getUsername() + "!</h2><br/><br/>");
		builder.append("<p>W celu aktywacji konta kliknij na poniższy link: <br/>");
		builder.append(
				"<a href=\"http://localhost:8080/registration/activate?hash=" + token.getTokenHash() + "\">Klik</a><br/>");
		builder.append("Jeżeli to nie ty zakładałeś to konto to zignoruj tą wiadomość");

		return builder.toString();
	}
}
