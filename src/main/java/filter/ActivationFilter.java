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

import ejb.ActivateRepository;
import ejb.LoginRepository;
import model.User;
import util.CustomPrincipal;

/**
 * Servlet Filter implementation class ActivationFilter
 */
@WebFilter("/ActivationFilter")
public class ActivationFilter implements Filter {

	@Inject
	private LoginRepository repo;
	
	@Inject
	private ActivateRepository aRepo;
	
    /**
     * Default constructor. 
     */
    public ActivationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		CustomPrincipal principal = (CustomPrincipal)req.getUserPrincipal();
		if(principal!=null) {
			User user = repo.get(principal.getName());
			if(!user.isActive()) {
				System.out.println("ActivationFilter: User isn't active");
				req.getRequestDispatcher("activate.jsp").forward(req, (HttpServletResponse)response);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
