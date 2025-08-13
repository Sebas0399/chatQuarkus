package websocket;

import io.quarkus.logging.Log;
import io.quarkus.websockets.next.*;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;

import java.util.Objects;

@WebSocket(path = "/chat/{contactId}")
public class ChatWebSocket {

	// Declare the type of messages that can be sent and received
	public enum MessageType {USER_JOINED, USER_LEFT, CHAT_MESSAGE}

	public record ChatMessage(MessageType type, String from, String message) {
	}

	@Inject
	WebSocketConnection connection;
	@Inject
	OpenConnections openConnections;

	@OnOpen(broadcast = true)
	public ChatMessage onOpen() {
		Log.info("onOppen: " + connection);

		return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("contactId"), null);
	}

	@OnClose
	public void onClose() {
		ChatMessage departure = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("contactId"), null);
		connection.broadcast().sendTextAndAwait(departure);
	}

	@OnTextMessage(broadcast = true)
	public ChatMessage onMessage(ChatMessage message) {
		Log.info("onMessage: " + message);
		return message;
	}

	public void sendToContact(String contactId, Object message) {
		Log.info("Intentando enviar mensaje a contacto " + contactId);
		Log.info("Lista de conexiones"+openConnections.listAll());

		openConnections.listAll().forEach(
			conn -> {
				Log.info("Conexión encontrada, enviando..."+conn.pathParam("contactId"));
				if(conn.pathParam("contactId").toString().equals(contactId)) {
					conn.sendTextAndAwait(Json.encode(message));
				}
			}
		);
		//

	}

}
