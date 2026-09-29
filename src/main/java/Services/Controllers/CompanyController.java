package Services.Controllers;

import Application.Contracts.ICompanyService;
import Application.Entities.SaveCompanyRequest;
import Domain.Models.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class CompanyController {
    @Inject
    ICompanyService companyService;

    @POST
    @Path(("/save"))
    public Response<Boolean> register(SaveCompanyRequest request) {
        return Response.success(companyService.saveCompany(request));
    }
}
