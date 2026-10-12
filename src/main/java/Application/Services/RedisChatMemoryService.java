package Application.Services;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.redis.RedisChatMemoryStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RedisChatMemoryService {

    private final ChatMemoryStore chatMemoryStore;

    public RedisChatMemoryService() {
        // Conexión a Redis en localhost:6379 con TTL opcional o persistencia de memoria de chat
        this.chatMemoryStore = RedisChatMemoryStore.builder()
                .host("localhost")
                .port(6379)
                .build();
    }

    public ChatMemoryStore getChatMemoryStore() {
        return chatMemoryStore;
    }

    public List<ChatMessage> getMessages(Object memoryId) {
        return chatMemoryStore.getMessages(memoryId);
    }

    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        chatMemoryStore.updateMessages(memoryId, messages);
    }

    public void deleteMessages(Object memoryId) {
        chatMemoryStore.deleteMessages(memoryId);
    }
}
