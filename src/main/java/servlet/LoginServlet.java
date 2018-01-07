package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

/**
 * Servlet obsługujący żądania modułu logowania użytkownika
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	/**
	 * Metoda obsługująca żądania GET. Jeżeli użytkownik jest już zapisany jako
	 * atrybut sesji przekierowuje do strony głównej
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

	/*
	 * Metoda obsługująca żądania POST, przetwarzająca dane wysłane w formularzu
	 * logowania.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (username != null && password != null) {
				request.login(username, password);
			}

			response.sendRedirect(request.getContextPath() + "/");

		} catch (ServletException ex) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
}
