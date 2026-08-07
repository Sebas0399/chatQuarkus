package Services.Controllers;

import db.Company;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import Application.Services.JwtService;
import repo.CompaniesRepository;

import java.util.List;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class UserController {

	@Inject
	JwtService jwtService;
	@POST
	@Path(("/login"))
	public Response login(JsonObject body){

		return Response.ok(jwtService.generateToken(body.getString("user"))).build();

	}

}
