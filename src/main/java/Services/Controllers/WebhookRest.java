package Services.Controllers;

import java.util.concurrent.CompletableFuture;

import Application.Contracts.IAssistantService;
import Application.Contracts.ICompanyService;
import Application.Contracts.IContactService;
import Application.Contracts.IMessageService;
import Application.Entities.SaveMessageRequest;
import Application.ViewModels.AssistantViewModel;
import Application.ViewModels.ContactViewModel;
import Domain.Models.Company;
import io.quarkus.logging.Log;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.assistant.AssistantFactory;
import websocket.ChatWebSocket;
import websocket.ContactWebSocket;

@Path("/webhook")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class WebhookRest {

    private static final String DEFAULT_VERIFY_TOKEN = "MDm9WKPcEVFIn4lgJzBaUZsQwBczAtSB";

    @Inject
    IContactService contactService;

    @Inject
    IMessageService messageService;

    @Inject
    ICompanyService companyService;

    @Inject
    IAssistantService assistantService;

    @Inject
    AssistantFactory assistantFactory;

    @Inject
    ChatWebSocket chatWebSocket;

    @Inject
    ContactWebSocket contactWebSocket;

    /**
     * WhatsApp Webhook verification endpoint with path token.
     */
    @GET
    @Path("/whatsapp/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response verifyWebhook(
            @PathParam("token") String pathToken,
            @QueryParam("hub.mode") String mode,
            @QueryParam("hub.challenge") String challenge,
            @QueryParam("hub.verify_token") String verifyToken) {

        if (mode != null && "subscribe".equals(mode) && isTokenValid(pathToken, verifyToken)) {
            Log.infof("WhatsApp webhook verified successfully for token: %s", pathToken);
            return Response.ok(challenge).build();
        } else {
            Log.warnf("WhatsApp webhook verification failed for mode: %s, token: %s", mode, verifyToken);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     * WhatsApp Webhook verification endpoint without path token.
     */
    @GET
    @Path("/whatsapp")
    @Produces(MediaType.TEXT_PLAIN)
    public Response verifyWebhookGeneral(
            @QueryParam("hub.mode") String mode,
            @QueryParam("hub.challenge") String challenge,
            @QueryParam("hub.verify_token") String verifyToken) {

        if (mode != null && "subscribe".equals(mode) && isTokenValid(null, verifyToken)) {
            return Response.ok(challenge).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     * Receiver for WhatsApp Cloud API events (messages, status receipts).
     */
    @POST
    @Path("/whatsapp/{token}")
    public Response receiveWhatsAppMessage(@PathParam("token") String token, JsonObject body) {
        if (body == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Payload cannot be null").build();
        }

        Company company = companyService.findByWebhookToken(token);
        Integer companyId = company != null ? company.getId() : null;

        String objectType = body.getString("object");
        if ("whatsapp_business_account".equals(objectType)) {
            JsonArray entryArray = body.getJsonArray("entry");
            if (entryArray == null || entryArray.isEmpty()) {
                return Response.ok().build();
            }

            for (int i = 0; i < entryArray.size(); i++) {
                JsonObject entry = entryArray.getJsonObject(i);
                JsonArray changesArray = entry.getJsonArray("changes");
                if (changesArray == null || changesArray.isEmpty()) {
                    continue;
                }

                for (int j = 0; j < changesArray.size(); j++) {
                    JsonObject change = changesArray.getJsonObject(j);
                    JsonObject value = change.getJsonObject("value");
                    if (value == null) {
                        continue;
                    }

                    // Process incoming messages
                    if (value.containsKey("messages")) {
                        processIncomingMessages(value, companyId);
                    }

                    // Process message delivery / read status receipts
                    if (value.containsKey("statuses")) {
                        processStatusUpdates(value);
                    }
                }
            }
        }

        return Response.ok().build();
    }

    /**
     * Health check endpoint for the webhook module.
     */
    @GET
    @Path("/status")
    public Response status() {
        return Response.ok(JsonObject.of("status", "UP", "service", "WebhookRest")).build();
    }

    /**
     * Endpoint to simulate/test incoming messages without external WhatsApp triggers.
     */
    @POST
    @Path("/simulate")
    public Response simulateIncomingMessage(JsonObject payload) {
        String phone = payload.getString("from", "50600000000");
        String name = payload.getString("name", "Test User");
        String text = payload.getString("text", "Hello");
        Integer companyId = payload.getInteger("companyId", 1);

        ContactViewModel contact = contactService.findOrCreateContact(companyId, phone, name);

        SaveMessageRequest messageRequest = SaveMessageRequest.builder()
                .text(text)
                .companyId(companyId)
                .contactId(contact.getId())
                .isFromContact(true)
                .isFromCompany(false)
                .build();
        messageService.saveMessage(messageRequest);

        sendToWebSockets(contact, text, "CONTACT");
        triggerAiAssistant(companyId, contact, text);

        return Response.ok(JsonObject.of("success", true, "contactId", contact.getId())).build();
    }

    // --- Private Helper Methods ---

    private void processIncomingMessages(JsonObject value, Integer companyId) {
        JsonArray messages = value.getJsonArray("messages");
        if (messages == null || messages.isEmpty()) {
            return;
        }

        String contactName = "Unknown";
        String waId = null;

        JsonArray contacts = value.getJsonArray("contacts");
        if (contacts != null && !contacts.isEmpty()) {
            JsonObject contactObj = contacts.getJsonObject(0);
            waId = contactObj.getString("wa_id");
            JsonObject profile = contactObj.getJsonObject("profile");
            if (profile != null && profile.containsKey("name")) {
                contactName = profile.getString("name");
            }
        }

        for (int k = 0; k < messages.size(); k++) {
            JsonObject messageObj = messages.getJsonObject(k);
            if (waId == null) {
                waId = messageObj.getString("from");
            }

            String messageText = extractMessageText(messageObj);
            if (messageText == null || messageText.isBlank()) {
                continue;
            }

            ContactViewModel contact = contactService.findOrCreateContact(companyId, waId, contactName);

            // Persist the incoming message
            SaveMessageRequest request = SaveMessageRequest.builder()
                    .text(messageText)
                    .companyId(companyId)
                    .contactId(contact.getId())
                    .isFromContact(true)
                    .isFromCompany(false)
                    .build();
            messageService.saveMessage(request);

            // Notify WebSockets
            sendToWebSockets(contact, messageText, contact.getName() != null ? contact.getName() : "CONTACT");

            // Trigger AI assistant if configured
            if (companyId != null) {
                triggerAiAssistant(companyId, contact, messageText);
            }
        }
    }

    private void processStatusUpdates(JsonObject value) {
        JsonArray statuses = value.getJsonArray("statuses");
        if (statuses != null) {
            for (int i = 0; i < statuses.size(); i++) {
                JsonObject status = statuses.getJsonObject(i);
                Log.infof("WhatsApp message status update: id=%s, status=%s, recipient=%s",
                        status.getString("id"),
                        status.getString("status"),
                        status.getString("recipient_id"));
            }
        }
    }

    private String extractMessageText(JsonObject messageObj) {
        String type = messageObj.getString("type", "text");
        if ("text".equals(type)) {
            JsonObject textObj = messageObj.getJsonObject("text");
            return textObj != null ? textObj.getString("body") : null;
        } else if ("interactive".equals(type)) {
            JsonObject interactive = messageObj.getJsonObject("interactive");
            if (interactive != null) {
                JsonObject buttonReply = interactive.getJsonObject("button_reply");
                if (buttonReply != null) return buttonReply.getString("title");
                JsonObject listReply = interactive.getJsonObject("list_reply");
                if (listReply != null) return listReply.getString("title");
            }
        }
        return "[" + type + "]";
    }

    private void sendToWebSockets(ContactViewModel contact, String messageText, String from) {
        try {
            contactWebSocket.sendToContact(
                    String.valueOf(contact.getId()),
                    new ContactWebSocket.ChatMessage(
                            ContactWebSocket.MessageType.CHAT_MESSAGE,
                            from,
                            messageText));
        } catch (Exception e) {
            Log.error("Error sending message to contact WebSocket", e);
        }

        try {
            contactService.updateNotification(contact.getId(), true);
            chatWebSocket.updateNotification(contact.getId());
        } catch (Exception e) {
            Log.error("Error updating notification WebSocket", e);
        }
    }

    private void triggerAiAssistant(Integer companyId, ContactViewModel contact, String incomingText) {
        AssistantViewModel assistantVM = assistantService.getAssistant(companyId);
        if (assistantVM == null || assistantVM.getIaProvider() == null) {
            Log.debugf("No active IA provider configured for companyId: %d", companyId);
            return;
        }

        var assistantProvider = assistantFactory.getProvider(assistantVM.getIaProvider());
        if (assistantProvider == null) {
            Log.warnf("Assistant provider not found for IA type: %s", assistantVM.getIaProvider());
            return;
        }

        final Integer targetContactId = contact.getId();
        final String contactDisplayName = contact.getName() != null ? contact.getName() : "ASSISTANT";

        CompletableFuture.supplyAsync(() -> {
            try {
                return assistantProvider.response(incomingText, companyId);
            } catch (Exception e) {
                Log.error("Error generating assistant response asynchronously", e);
                return null;
            }
        }).thenAccept(responseText -> {
            if (responseText == null || responseText.isBlank()) {
                return;
            }
            try {
                SaveMessageRequest assistantRequest = SaveMessageRequest.builder()
                        .text(responseText)
                        .companyId(companyId)
                        .contactId(targetContactId)
                        .isFromContact(false)
                        .isFromCompany(true)
                        .build();
                messageService.saveMessage(assistantRequest);

                try {
                    contactWebSocket.sendToContact(
                            String.valueOf(targetContactId),
                            new ContactWebSocket.ChatMessage(
                                    ContactWebSocket.MessageType.CHAT_MESSAGE,
                                    contactDisplayName,
                                    responseText));
                } catch (Exception e) {
                    Log.error("Error sending assistant response to contact WebSocket", e);
                }

                try {
                    contactService.updateNotification(targetContactId, true);
                    chatWebSocket.updateNotification(targetContactId);
                } catch (Exception e) {
                    Log.error("Error updating notification after assistant response", e);
                }
            } catch (Exception e) {
                Log.error("Error persisting assistant response", e);
            }
        });
    }

    private boolean isTokenValid(String pathToken, String verifyToken) {
        if (DEFAULT_VERIFY_TOKEN.equals(verifyToken)) {
            return true;
        }
        if (verifyToken != null && verifyToken.equals(pathToken)) {
            return true;
        }
        if (verifyToken != null) {
            Company company = companyService.findByWebhookToken(verifyToken);
            if (company != null) return true;
        }
        if (pathToken != null) {
            Company company = companyService.findByWebhookToken(pathToken);
            if (company != null) return true;
        }
        return false;
    }
}
