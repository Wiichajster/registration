package endpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejb.AccountRepository;

@RequestScoped
@Path("/activate")
@Produces(MediaType.APPLICATION_JSON)
public class ActivationEndpoint {

	@Inject
	private AccountRepository repo;

	@GET
	@Path("/{hash}")
	public Response activateUser(@PathParam("hash") String hash) {
		if (repo.activateUser(hash)) {
			return Response.ok().build();
		} else {
			return Response.status(400).build();
		}
	}
}
