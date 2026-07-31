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
import java.util.concurrent.CompletableFuture;
import jakarta.transaction.UserTransaction;
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
    @Inject
    UserTransaction userTransaction;
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
                message.setCompany(contactoEncontrado.getCompany());;
                message.setContact(contactoEncontrado);
                message.setIsFromContact(true);
                messagesRepository.persistAndFlush(message);
                // emitimos al contacto (manejo de errores para no romper el flujo)
                try {
                    contactWebSocket.sendToContact(
                            String.valueOf(contactoEncontrado.getId()),
                            new ContactWebSocket.ChatMessage(
                                    ContactWebSocket.MessageType.CHAT_MESSAGE,
                                    contactoEncontrado.getName() != null ? contactoEncontrado.getName() : "CONTACT",
                                    message.getText()));
                } catch (Exception e) {
                    Log.error("Error sending message to contact WebSocket", e);
                }
                // emitimos al canal de notificaciones
                try {
                    chatWebSocket.updateNotification(contactoEncontrado.getId());
                    contactoEncontrado.setHasNotification(true);
                    contactsRepository.persist(contactoEncontrado);
                } catch (Exception e) {
                    Log.error("Error updating notification / persisting contact notification flag", e);
                }
                //obtener el proveedor de ia activo
                Optional<IA_TYPE> iaType=assistantService.getActiveIaProvider(contactoEncontrado.getCompany().getId());
                if (iaType.isEmpty()) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("No IA provider configured for this company")
                            .build();
                }
                var assistant = assistantFactory.getProvider(iaType.get());
                // Llamada asíncrona a la IA para no bloquear el webhook
                CompletableFuture.supplyAsync(() -> {
                    try {
                        return assistant.response(message.getText(), contactoEncontrado.getCompany().getId());
                    } catch (Exception e) {
                        Log.error("Error calling assistant provider asynchronously", e);
                        return null;
                    }
                }).thenAcceptAsync(responseText -> {
                    if (responseText == null) return;
                    try {
                        userTransaction.begin();
                        Message assistantMessage = new Message();
                        assistantMessage.setText(responseText);
                        assistantMessage.setCompany(contactoEncontrado.getCompany());
                        assistantMessage.setContact(contactoEncontrado);
                        assistantMessage.setIsFromContact(false);
                        messagesRepository.persistAndFlush(assistantMessage);
                        try {
                            contactWebSocket.sendToContact(
                                    String.valueOf(contactoEncontrado.getId()),
                                    new ContactWebSocket.ChatMessage(
                                            ContactWebSocket.MessageType.CHAT_MESSAGE,
                                            contactoEncontrado.getName() != null ? contactoEncontrado.getName() : "ASSISTANT",
                                            responseText));
                        } catch (Exception e) {
                            Log.error("Error sending assistant response to contact WebSocket", e);
                        }
                        try {
                            chatWebSocket.updateNotification(contactoEncontrado.getId());
                            contactoEncontrado.setHasNotification(true);
                            contactsRepository.persist(contactoEncontrado);
                        } catch (Exception e) {
                            Log.error("Error updating notification after assistant response", e);
                        }
                        userTransaction.commit();
                    } catch (Exception e) {
                        Log.error("Error persisting assistant response", e);
                        try {
                            userTransaction.rollback();
                        } catch (Exception ex) {
                            Log.error("Error rolling back transaction for assistant response", ex);
                        }
                    }
                });
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
