package filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ejb.LoginRepository;
import model.User;

//@WebFilter("/*")
public class LoginFilter implements Filter {

	@Inject
	private LoginRepository repo;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		System.out.println("Login filter: przed ifem");
		if (user == null) {
			System.out.println("Login filter: za ifem");
			//resp.sendRedirect(req.getContextPath() + "/login");
			/*
			 * if (user.isActive()) { req.getSession(true).setAttribute("user", user); }else
			 * { req.getRequestDispatcher("activate.jsp").forward(req,
			 * (HttpServletResponse)response); }
			 */
		}

		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
