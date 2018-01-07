package servlet;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import ejb.RegistrationReository;
import exceptions.UserAlreadyExistException;
import model.User;
import util.HashingHelper;

/**
 * Servlet obsługujący zadania rejestracji użytkownika
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private RegistrationReository repo;

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
		Set<String> errorMessages = new HashSet<>();

		if (password != null) {
			password = HashingHelper.hashString(password);
		}

		User user = new User(username, password, email);

		try {
			if(repo.add(user)) {
				response.sendRedirect(request.getContextPath() + "/");
			}
		} catch (ConstraintViolationException ex) {
			System.out.println("Registration servlet: Constraint violation");
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for(ConstraintViolation<?> v: violations) {
				errorMessages.add(v.getMessage());
			}
			
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		} catch (UserAlreadyExistException ex) {
			errorMessages.add("Nazwa użytkownika jest już zajęta");
			
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		} catch (MessagingException ex) {
			errorMessages.add("Wystąpił nieoczekiwany błąd podczas wysyłania wiadomości aktywacyjnej");
			
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}
	}

}
