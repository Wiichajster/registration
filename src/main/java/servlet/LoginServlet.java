package servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ejb.LoginRepository;
import model.User;
import util.AuthHelper;
import util.CustomPrincipal;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private LoginRepository repo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);

		User user = (User) session.getAttribute("user");

		if (user != null) {
			response.sendRedirect(request.getContextPath() + "/");
		} else {
			response.sendError(403);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (username != null && password != null) {
				request.login(username, password);
			}

			CustomPrincipal principal = (CustomPrincipal) request.getUserPrincipal();

			System.out.println("LoginServlet: Nazwa użytkownika: " + principal.getName());
			System.out.println("LoginServlet: Czy aktywny: " + principal.isActivated());

			response.sendRedirect(request.getContextPath() + "/");

		} catch (ServletException ex) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
		/*
		 * User user = repo.get(username);
		 * 
		 * boolean isValidated = false; try { isValidated = AuthHelper.validate(user,
		 * password); } catch (NoSuchAlgorithmException e) {
		 * request.setAttribute("errorMessage", "Wystąpił błąd w czasie logowania"); }
		 * 
		 * if (isValidated) { System.out.println("Login servlet: isValidated");
		 * HttpSession session = request.getSession(true); session.setAttribute("user",
		 * user); response.sendRedirect(request.getContextPath() + "/"); } else {
		 * request.setAttribute("errorMessage", "Niepoprawny login lub hasło"); }
		 */

	}
}
