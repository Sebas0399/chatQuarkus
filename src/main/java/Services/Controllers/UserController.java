package Services.Controllers;

import jakarta.ws.rs.core.MediaType;
import Application.Contracts.IUserService;
import Application.Entities.UserRegisterRequest;
import Domain.Models.Response;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
@PermitAll
public class UserController {
    @Inject
    IUserService userService;

    @POST
    @Path(("/register"))
    @PermitAll
    public Response<Boolean> register(UserRegisterRequest request) {
        return Response.success(userService.saveUser(request));
    }
}
