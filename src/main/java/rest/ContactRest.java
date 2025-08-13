package rest;

import dtos.ContactDto;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import repo.ContactsRepository;
import service.CompanyService;

import java.util.List;

@Path("/contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class ContactRest {
    @Inject
    ContactsRepository contactsRepository;
    @Inject
    CompanyService companyService;

    @GET
    @Path("/company/{companyId}")
    public List<ContactDto> findByCompany(@PathParam("companyId") Integer companyId){
        Log.info(companyService.generateTokenWebhook());
        var contactos=contactsRepository.findByCompanyId(companyId);
        return contactos.stream().map(obj->{
            ContactDto dto=new ContactDto();
            dto.setCompany_id(obj.getCompany_id());
            dto.setId(obj.getId());
            dto.setName(obj.getName());
            return dto;
        }).toList();
    }
}
