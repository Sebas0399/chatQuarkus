package Services.Controllers;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import Application.Contracts.IContactService;
import Application.Contracts.IUserContextService;
import Application.Entities.ContactFilter;
import Application.Entities.PagedRequest;
import Application.ViewModels.ContactViewModel;
import Domain.Models.PageResponse;
import Domain.Models.Response;

@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
@Authenticated
public class ContactController {
    @Inject
    IContactService contactsService;
    @Inject
    IUserContextService userContext;

    @POST
    @Path("/byCompany")
    public Response<PageResponse<ContactViewModel>> findByCompany(PagedRequest<ContactFilter> request){
        if (request.getFilter() == null) {
            request.setFilter(new ContactFilter());
        }
        request.getFilter().setCompanyId(userContext.getCompanyId());
        return Response.success(contactsService.findByCompanyId(request));
    }
}
