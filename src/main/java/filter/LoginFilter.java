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
import util.CustomPrincipal;

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
		
		CustomPrincipal principal = (CustomPrincipal)req.getUserPrincipal();
		
		if(principal!=null) {
			if(!principal.isActivated()) {
				System.out.println("LoginFilter: Użytkownik nieaktywny");
			}else {
				System.out.println("LoginFilter: Użytkownik aktywny");
			}
		}
		

		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
