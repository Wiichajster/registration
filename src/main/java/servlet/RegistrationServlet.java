package servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.LoginRepository;
import exceptions.UserAlreadyExistException;
import model.User;
import util.HashingHelper;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private LoginRepository repo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("registration.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");

		if (password != null) {
			try {
				password = HashingHelper.hashString(password);
			} catch (NoSuchAlgorithmException ex) {
				request.setAttribute("errorMessage", "Wystąpił nieoczekiwany błąd podczas rejestracji");
				request.getRequestDispatcher("registration.jsp").forward(request, response);
			}
		}

		User user = new User(username, password, email);

		try {
			repo.add(user);
		} catch (UserAlreadyExistException ex) {
			request.setAttribute("errorMessage", "Nazwa użytkownika jest już zajęta");
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		} catch (EJBException ex) {
			request.setAttribute("errorMessage", "Wystąpił nieoczekiwany błąd podczas rejestracji");
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}

		response.sendRedirect(request.getContextPath() + "/");
	}

}
