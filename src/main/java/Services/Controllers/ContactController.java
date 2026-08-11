package Services.Controllers;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import repo.ContactsRepository;
import service.CompanyService;

import java.util.List;

import Application.Contracts.IContactService;
import Application.Services.ContactService;
import Application.ViewModels.ContactViewModel;
import Domain.Models.Contact;
import Domain.Models.Response;

@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class ContactController {
    @Inject
    IContactService contactsService;
  


    @GET
    @Path("/company/{companyId}")
    public Response<List<ContactViewModel>> findByCompany(@PathParam("companyId") Integer companyId){
        return Response.success(contactsService.findByCompanyId(companyId));
    }
}
