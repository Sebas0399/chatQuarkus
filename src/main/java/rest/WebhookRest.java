package rest;

import db.Contact;
import db.Message;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import repo.ContactsRepository;
import repo.MessagesRepository;
import service.assistant.AssistantFactory;
import service.assistant.AssistantService;
import service.assistant.IA_TYPE;
import websocket.ChatWebSocket;
import websocket.ContactWebSocket;

import static io.vertx.core.json.JsonObject.mapFrom;
import static websocket.ChatWebSocket.MessageType.CHAT_MESSAGE;

import java.util.Optional;

@Path("/webhook")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class WebhookRest {
    @Inject
    ContactsRepository contactsRepository;
    @Inject
    MessagesRepository messagesRepository;
    @Inject
    ChatWebSocket chatWebSocket;
    @Inject
    ContactWebSocket contactWebSocket;
    @Inject 
    AssistantService assistantService;
    @Inject 
    AssistantFactory assistantFactory;
    @GET
    @Path("/whatsapp/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response verifyWebhook(
            @QueryParam("hub.mode") String mode,
            @QueryParam("hub.challenge") String challenge,
            @QueryParam("hub.verify_token") String token) {

        if (mode != null && token != null && mode.equals("subscribe")
                && token.equals("MDm9WKPcEVFIn4lgJzBaUZsQwBczAtSB")) {
            return Response.ok(challenge).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("whatsapp/{token}")
    // @Transactional
    public Response receive(@PathParam("token") String token, JsonObject body) {

        if (body.getString("object").equals("whatsapp_business_account")) {
            var entry = JsonObject.mapFrom(body.getJsonArray("entry").getValue(0));
            var changes = JsonObject.mapFrom(entry.getJsonArray("changes").getValue(0));
            var value = changes.getJsonObject("value");
            var contacts = JsonObject.mapFrom(value.getJsonArray("contacts").getValue(0));
            var messages = JsonObject.mapFrom(value.getJsonArray("messages").getValue(0));
            Log.info(contacts.getJsonObject("profile"));
            var contactoEncontrado = contactsRepository.findByNumber(contacts.getString("wa_id"));
            if (contactoEncontrado != null) {
                Message message = new Message();
                message.setText(messages.getJsonObject("text").getString("body"));
                message.setContact_id(contactoEncontrado.getId());
                message.setCompany_id(contactoEncontrado.getCompany_id());
                message.setIsFromContact(true);
                messagesRepository.persist(message);
                // emitimos al contacto
                contactWebSocket.sendToContact(
                        String.valueOf(contactoEncontrado.getId()),
                        new ContactWebSocket.ChatMessage(
                                ContactWebSocket.MessageType.CHAT_MESSAGE,
                                "CONTACT", // o el nombre del remitente
                                message.getText()));
                // emitimos al canal de notificaciones
                chatWebSocket.updateNotification(contactoEncontrado.getId());
                contactoEncontrado.setHasNotification(true);
                contactoEncontrado.persist();
                //obtener el proveedor de ia activo
                Optional<IA_TYPE> iaType=assistantService.getActiveIaProvider(contactoEncontrado.getCompany_id());
                if(iaType.isEmpty){

                }
                var assistant=assistantFactory.getProvider(iaType);
                assistant.response(message.getText());
                //analizamos la intencion del usuario

                //respondemos

            } else {
                Contact contact = new Contact();
                contact.setNumber(contacts.getString("wa_id"));
                contact.setName(contacts.getJsonObject("profile").getString("name"));
                contactsRepository.persist(contact);
                Log.info("Contacto creado");
            }

            // contact.setName();
            // contactsRepository.persist();
        }
        return Response.status(Response.Status.OK).build();

    }
}
