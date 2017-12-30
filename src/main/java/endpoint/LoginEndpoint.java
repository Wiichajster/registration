package endpoint;

import java.io.IOException;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ejb.LoginRepository;
import exceptions.UserAlreadyExistException;
import model.User;

@RequestScoped
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginEndpoint {

	@Inject
	private LoginRepository repo;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByUsername(String username) {
		User user = repo.get(username);
		if (user == null) {
			return Response.noContent().build();
		} else {
			return Response.ok(user).build();
		}

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user) throws EJBException, UserAlreadyExistException {
		if (user != null) {
			repo.add(user);
			return Response.ok().build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void createUser(@FormParam("username") String username, @FormParam("password") String password,
			@FormParam("email") String email, @Context HttpServletRequest request,
			@Context HttpServletResponse response) throws UserAlreadyExistException, IOException {
		User user = new User(username, password, email);
		repo.add(user);
		response.sendRedirect(request.getContextPath() + "/");

	}
}
