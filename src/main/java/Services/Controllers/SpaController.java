package Services.Controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

@Path("/{path:.*}")
@ApplicationScoped
public class SpaController {

    @GET
    public Response forward(@jakarta.ws.rs.PathParam("path") String path) {
        // Permitir que las rutas de API o recursos estáticos reales pasen sin redirección
        if (path.startsWith("api/") || path.startsWith("q/") || path.contains(".")) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/resources/index.html");
            if (is != null) {
                return Response.ok(is).type("text/html").build();
            }
        } catch (Exception e) {
            // Ignorar y retornar 404 si no se encuentra
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
