package service.assistant;

import db.Assistant;
import dev.langchain4j.model.chat.ChatLanguageModel;

public interface IAssistant {
    String response(String message, Integer companyId);

    ChatLanguageModel buildChatModel(Assistant assistant);
}
