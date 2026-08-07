package Services.ExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import Domain.Models.DomainException;

public class GlobalExceptionHandler {
    // 1. Maneja excepciones de dominio (400 Bad Request o 404 Not Found)
    @ServerExceptionMapper
    public Response mapGeneralException(Throwable ex) {
        // Convertir la traza del error (stacktrace) a un String
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));

        // Estructurar los detalles del error en un mapa ordenado
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("exceptionClass", ex.getClass().getName());
        errorDetails.put("detailMessage", ex.getMessage());
        errorDetails.put("stackTrace", sw.toString().split("\r?\n")); // Convierte el stacktrace en una lista de líneas
                                                                      // JSON

        return Response.status(Status.INTERNAL_SERVER_ERROR)
            .entity(Domain.Models.Response.error(
                "Error interno del servidor",
                Status.INTERNAL_SERVER_ERROR.getStatusCode(),
            errorDetails))
            .build();
    }
}
