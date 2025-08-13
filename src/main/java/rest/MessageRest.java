package rest;

import db.Message;
import dtos.ContactDto;
import dtos.MessageDto;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import repo.MessagesRepository;

import java.util.List;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class MessageRest {
    @Inject
    MessagesRepository messagesRepository;
    @GET
    @Path("/contact/{contactId}")
    public List<MessageDto> findByContactId(@PathParam("contactId") Integer contactId){
        var messages=messagesRepository.findByContactId(contactId);
        return messages.stream().map(obj->{
            MessageDto dto=new MessageDto();
            dto.setCompany_id(obj.getCompany_id());
            dto.setId(obj.getId());
            dto.setIsFromContact(obj.getIsFromContact());
            dto.setText(obj.getText());
            dto.setContact_id(obj.getContact_id());
            dto.setIsFromCompany(obj.getIsFromCompany());
            return dto;
        }).toList();
    }
}
