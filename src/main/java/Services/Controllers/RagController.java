package Services.Controllers;

import Application.Services.RagService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/rag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RagController {

    @Inject
    RagService ragService;

    @POST
    @Path("/ingest")
    public Response ingest(Map<String, Object> payload) {
        try {
            String text = (String) payload.get("text");
            String title = (String) payload.get("title");
            Integer companyId = Integer.parseInt(payload.get("companyId").toString());

            ragService.ingestDocument(text, title, companyId);
            return Response.ok(Map.of("message", "Document ingested successfully into Elasticsearch")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", e.getMessage())).build();
        }
    }
}
