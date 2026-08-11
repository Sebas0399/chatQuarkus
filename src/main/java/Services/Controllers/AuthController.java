package Services.Controllers;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import Application.Entities.AuthRequest;
import Application.Services.AuthService;


@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class AuthController {

	@Inject
	AuthService authService;
	@POST
	@Path(("/login"))
	@PermitAll
	public Response login(AuthRequest request) {
		return Response.ok(authService.login(request)).build();
	}

}
