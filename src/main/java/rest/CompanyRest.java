package rest;

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
import repo.CompaniesRepository;
import service.JwtService;

import java.util.List;

@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class CompanyRest {

	@Inject
	JwtService jwtService;
	@Inject
	CompaniesRepository companiesRepository;
	@GET
	public List<Company> getCompanies() {
		return  companiesRepository.listAll();
	}

	@POST
	@Path(("/login"))
	public Response login(JsonObject body){

		return Response.ok(jwtService.generateToken(body.getString("user"))).build();

	}

}
