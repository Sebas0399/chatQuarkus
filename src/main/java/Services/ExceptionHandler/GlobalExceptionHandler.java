package Services.ExceptionHandler;

import Domain.Models.DomainException;
import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Response handleDomainException(DomainException ex) {
        return Response.status(Status.BAD_REQUEST)
                .entity(Domain.Models.Response.error(ex.getMessage(), Status.BAD_REQUEST.getStatusCode()))
                .build();
    }

    @ServerExceptionMapper
    public Response handleGeneralException(Throwable ex) {
        Log.error("Error no controlado", ex);
        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(Domain.Models.Response.error("Error interno del servidor", Status.INTERNAL_SERVER_ERROR.getStatusCode()))
                .build();
    }
}
