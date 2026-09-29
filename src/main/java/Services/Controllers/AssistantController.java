package Services.Controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

import Application.Contracts.IAssistantService;
import Application.Contracts.IScriptEngineService;
import Application.Entities.SaveAssistantRequest;
import Application.Entities.TestScriptRequest;
import Application.Entities.TestToolResponse;
import Application.ViewModels.AssistantViewModel;
import Domain.Models.Response;

@Path("/assistants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class AssistantController {
    @Inject
    IAssistantService assistantService;
    @Inject
    IScriptEngineService scriptEngineService;

    @GET
    @Path("/{companyId}")
    public Response<AssistantViewModel> findByContactId(@PathParam("companyId") Integer companyId) {
        return Response.success(assistantService.getAssistant(companyId));
    }

    @POST
    public Response<Boolean> saveAssistant(SaveAssistantRequest request) {
        return Response.success(assistantService.saveAssistant(request));
    }

    @POST
    @Path("/tools/test")
    public Response testTool(TestScriptRequest request) {
        try {
            TestToolResponse result = scriptEngineService.testScript(request.getScript(), request.getArgsJson());
            return Response.success(Map.of("success", true, "output", result));
        } catch (Exception e) {
            // GraalVM entrega el error exacto de compilación/sintaxis y la línea
            return Response.success(Map.of(
                    "success", false,
                    "error", e.getMessage(),
                    "line", e.getLocalizedMessage() != null ? e.getLocalizedMessage() : 1));
        }
    }
}
