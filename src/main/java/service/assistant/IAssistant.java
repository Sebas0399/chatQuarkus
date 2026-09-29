package service.assistant;

import Infraestructure.Contracts.Entities.Assistant;
import dev.langchain4j.model.chat.ChatLanguageModel;

public interface IAssistant {
    String response(String message, Integer companyId);

    ChatLanguageModel buildChatModel(Domain.Models.Assistant assistant);
}
