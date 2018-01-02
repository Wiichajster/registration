package servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.ActivateRepository;

/**
 * Servlet implementation class ActivateServlet
 */
@WebServlet("/activate")
public class ActivateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private ActivateRepository repo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActivateServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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
