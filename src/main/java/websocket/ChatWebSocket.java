package websocket;

import io.quarkus.logging.Log;
import io.quarkus.websockets.next.*;
import jakarta.inject.Inject;

@WebSocket(path = "/chat")
public class ChatWebSocket {

    // Declare the type of messages that can be sent and received

    @Inject
    WebSocketConnection connection;
    @Inject
    OpenConnections openConnections;

    @OnOpen(broadcast = true)
    public void onOpen() {
        Log.info("onOppen: " + connection);

    }

    @OnClose
    public void onClose() {
        Log.info("onClose: " + connection);

    }

    @OnTextMessage
    public String onTextMessage(String message) {
        Log.info("onTextMessage: " + message);
        return message;
    }

    public void updateNotification(Integer contactId) {
        Log.info("Notiffiacion entrante" + contactId);

        //

    }

}
