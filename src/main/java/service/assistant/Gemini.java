package service.assistant;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import Domain.Contracts.IAssistantRepository;
import Application.Services.RagService;
import Application.Services.RedisChatMemoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Gemini implements IAssistant {
    @Inject
    private IAssistantRepository assistantsRepository;

    @Inject
    private RagService ragService;

    @Inject
    private RedisChatMemoryService redisChatMemoryService;

    @Override
    public String response(String message, Integer companyId) {
        Domain.Models.Assistant assistant = assistantsRepository.findByCompany(companyId);
        if(assistant == null) {
            throw new RuntimeException("No assistant found for the given company and type.");
        }

        // Configurar memoria de chat usando Redis (aislada por compañía/tenant)
        Object memoryId = "company_" + companyId;
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(redisChatMemoryService.getChatMemoryStore())
                .build();

        // Buscar contexto relevante en Elasticsearch usando RAG
        String context = ragService.retrieveRelevantContext(message, companyId);
        String finalSystemPrompt = assistant.getSystemPrompt();

        if (context != null && !context.isEmpty()) {
            finalSystemPrompt += "\n\nContexto relevante recuperado de la base de conocimiento:\n" + context;
        }

        // Añadir system prompt y mensaje del usuario a la memoria de chat persistida en Redis
        chatMemory.add(new SystemMessage(finalSystemPrompt));
        chatMemory.add(new UserMessage(message));

        ChatLanguageModel chatModel = buildChatModel(assistant);
        var response = chatModel.chat(ChatRequest.builder()
            .messages(chatMemory.messages())
            .build());

        var aiMessage = response.aiMessage();
        if (aiMessage != null) {
            chatMemory.add(aiMessage);
        }

        String text = aiMessage != null ? aiMessage.text() : null;
        System.out.println("Response from AI with RAG & Redis Memory: " + text);
        return text;
    }

    @Override
    public ChatLanguageModel buildChatModel(Domain.Models.Assistant assistant) {
        String apiKey = assistant.getToken();
        String modelName = assistant.getModel();
        String baseUrl = assistant.getUrl();

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
