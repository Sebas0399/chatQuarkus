package Services.Controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

import Application.Contracts.IAssistantService;
import Application.Contracts.IScriptEngineService;
import Application.Contracts.IUserContextService;
import Application.Entities.SaveAssistantRequest;
import Application.Entities.TestScriptRequest;
import Application.Entities.TestToolResponse;
import Application.ViewModels.AssistantViewModel;
import Domain.Models.Response;
import io.quarkus.security.Authenticated;

@Path("/assistants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
@Authenticated
public class AssistantController {
    @Inject
    IAssistantService assistantService;
    @Inject
    IScriptEngineService scriptEngineService;
    @Inject
    IUserContextService userContext;

    @GET
    public Response<AssistantViewModel> getAssistant() {
        return Response.success(assistantService.getAssistant(userContext.getCompanyId()));
    }

    @POST
    public Response<Boolean> saveAssistant(SaveAssistantRequest request) {
        request.setCompanyId(userContext.getCompanyId());
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
