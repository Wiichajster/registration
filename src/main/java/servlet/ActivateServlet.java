package servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.AccountRepository;

/**
 * Servlet obsługujący żądania do aktywacji konta użytkownika
 */
@WebServlet("/activate")
public class ActivateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private AccountRepository repo;

	public ActivateServlet() {
		super();
	}

	/**
	 * Metoda obsługująca żadanie wysyłane przez link aktywacyjny.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String hash = request.getParameter("hash");
		
		if (hash != null) {
			repo.activateUser(hash);

			response.sendRedirect(request.getContextPath() + "/");			
		} else {
			request.setAttribute("errorMessage", "Nieprawidłowy adres aktywacyjny. Proszę spróbować jeszcze raz.");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

}
