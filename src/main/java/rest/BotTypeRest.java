package rest;

import db.BotType;
import db.Company;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import repo.BotTypesRepository;

import java.util.List;

@Path("/botTypes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class BotTypeRest {
    @Inject
    BotTypesRepository botTypesRepository;

    @GET
    public List<BotType> getCompanies() {
        return  botTypesRepository.listAll();
    }

}
