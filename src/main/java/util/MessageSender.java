package util;

import javax.mail.MessagingException;

public interface MessageSender {
	public boolean sendHtmlEmail(String recipient, String title, String message) throws MessagingException;
}
