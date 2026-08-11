package Services.Controllers;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import Application.Contracts.IMessageService;
import Application.ViewModels.MessageViewModel;
import Domain.Models.Response;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class MessageController {
    @Inject
    IMessageService messageService;
    @GET
    @Path("/contact/{contactId}")
    public Response<List<MessageViewModel>>findByContactId(@PathParam("contactId") Integer contactId){
        return Response.success(messageService.findByContactId(contactId));
    }
}
