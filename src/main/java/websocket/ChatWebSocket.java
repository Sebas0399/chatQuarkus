package websocket;

import io.quarkus.logging.Log;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
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
        connection.getOpenConnections().forEach(conn -> {
            if (Objects.equals(contactId, conn.pathParam("contactId"))) {
                conn.sendTextAndAwait(message);
            }
        });

    }

}
