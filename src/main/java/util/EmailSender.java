package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@RequestScoped
public class EmailSender implements MessageSender {
	
	private static final String PROPERTIES_FILE = "src/main/resources/mail.properties";

	@Override
	public boolean sendHtmlEmail(String recipient, String title, String message) throws MessagingException {

		Properties props = new Properties(); //loadPropertiesFromFile(PROPERTIES_FILE);

		String username = "ddalecki88@gmail.com";//props.getProperty("username");
		String password = "!!Dam1988!!";//props.getProperty("password");
		boolean result = false;

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message mimeMessage = new MimeMessage(session);

			mimeMessage.setFrom(new InternetAddress(username));

			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));

			mimeMessage.setSubject(title);

			mimeMessage.setContent(message, "text/html");

			Transport.send(mimeMessage);

			result = true;
		} catch (MessagingException e) {
			throw new MessagingException("Wystąpił błąd w czasie wysyłania wiadomości", e);
		}

		return result;
	}

	private Properties loadPropertiesFromFile(String filename) {
		if (filename == null || "".equals(filename)) {
			return null;
		}

		Properties result = new Properties();

		try {
			result.load(new FileInputStream(filename));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}
}
