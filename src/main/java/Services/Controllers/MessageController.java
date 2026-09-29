package Services.Controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import Application.Contracts.IMessageService;
import Application.Contracts.IUserContextService;
import Application.Entities.SaveMessageRequest;
import Application.ViewModels.MessageViewModel;
import Domain.Models.Response;
import io.quarkus.security.Authenticated;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
@Authenticated
public class MessageController {
    @Inject
    IMessageService messageService;
    @Inject
    IUserContextService userContext;

    @GET
    @Path("/contact/{contactId}")
    public Response<List<MessageViewModel>> findByContactId(@PathParam("contactId") Integer contactId) {
        return Response.success(messageService.findByContactId(contactId));
    }

    @POST
    @Path("/send")
    public Response<Boolean> saveMessage(SaveMessageRequest request) {
        request.setCompanyId(userContext.getCompanyId());
        return Response.success(messageService.saveMessage(request));
    }
}
