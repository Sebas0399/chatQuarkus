package Services.Controllers;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import Application.Contracts.IContactService;
import Application.Entities.ContactFilter;
import Application.Entities.PagedRequest;
import Application.Services.ContactService;
import Application.ViewModels.ContactViewModel;
import Domain.Models.Contact;
import Domain.Models.PageResponse;
import Domain.Models.Response;

@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class ContactController {
    @Inject
    IContactService contactsService;
  



    @POST
    @Path("/byCompany")
    public Response<PageResponse<ContactViewModel>> findByCompany(PagedRequest<ContactFilter> request){
        return Response.success(contactsService.findByCompanyId(request));
    }

//     @POST
//     @Path("/paged")
//     public Response<PageResponse<ContactViewModel>> findPaged(PagedRequest<ContactFilter> request) {
//         return Response.success(contactsService.findPaged(request));
//     }
// }

//     @GET
//     @Path("/paged")
//     public Response<PageResponse<ContactViewModel>> findPaged(@BeanParam PagedRequest request) {
//         return Response.success(contactsService.findPaged(request));
//     }
}
