package websocket;

import io.quarkus.logging.Log;
import io.quarkus.websockets.next.*;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;

import java.util.Objects;

@WebSocket(path = "/chat}")
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
    public Boolean onTextMessage() {
        return true;
    }

    public void updateNotification(Integer contactId) {
        Log.info("Notiffiacion entrante" + contactId);

        //

    }

}
