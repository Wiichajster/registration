package servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.AccountRepository;

/**
 * Servlet implementation class RestartServlet
 */
@WebServlet("/restart")
public class RestartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private AccountRepository repo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RestartServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("restartPassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");

		if (email != null) {
			try {
				repo.restartPassword(email);
				request.getRequestDispatcher("restartMailSend.jsp").forward(request, response);
			} catch (MessagingException e) {
				request.setAttribute("errorMessage",
						"Wystąpił błąd w czasie wysyłania wiadomości. Proszę spróbować ponownie");
				request.getRequestDispatcher("restartPassword.jsp").forward(request, response);
			}
		}
	}

}
