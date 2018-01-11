package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;

import ejb.RegistrationRepository;
import model.User;
import util.HashingHelper;

/**
 * Servlet obsługujący zadania rejestracji użytkownika
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(RegistrationServlet.class);

	@Inject
	private RegistrationRepository repo;

	@Resource
	private Validator validator;

	public RegistrationServlet() {
		super();
	}

	/**
	 * W przypadku rządania GET przekieruj do pustego formularza rejestracyjnego
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("registration.jsp").forward(request, response);
	}

	/**
	 * Metoda przetwarzająca dane wysłanego formularza oraz wywołująca logikę
	 * dodania użytkownika do bazy danych
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");		

		if (password != null) {
			password = HashingHelper.hashString(password);
		}

		User user = new User(username, password, email);

		List<String> errorMessages = validateForm(user);
		
		if (errorMessages.isEmpty()) {
			try {
				repo.add(user);
			} catch (MessagingException e) {
				LOG.error("Błąd wysyłania aktywacyjnej wiadomości email");
				errorMessages.add("Błąd w trakcie wysyłania wiadomości aktywacyjnej. Proszę spróbować ponownie");
			}
		} else {
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		} 

		response.sendRedirect(request.getContextPath() + "/");
	}
	
	private List<String> validateForm(User user){
		Set<ConstraintViolation<User>> errors = null;
		List<String> errorMessages = new ArrayList<>();
		
		errors = validator.validate(user);
		
		for (ConstraintViolation<User> c : errors) {
			errorMessages.add(c.getMessage());
		}
		
		 return errorMessages;
	}

}
