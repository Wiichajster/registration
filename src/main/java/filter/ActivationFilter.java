package filter;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.AccountRepository;
import model.User;

public class ActivationFilter implements Filter {

	@Inject
	private AccountRepository repo;

	public ActivationFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		Principal principal = req.getUserPrincipal();
		if (principal != null) {
			User user = repo.get(principal.getName());
			if (!user.isActive()) {
				req.getRequestDispatcher("activate.jsp").forward(req, (HttpServletResponse) response);
			}
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
