package service.assistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import Domain.Contracts.IAssistantRepository;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class Gemini implements IAssistant {
    @Inject
    private IAssistantRepository assistantsRepository;
    @Override
    public String response(String message,Integer companyId) {
        Domain.Models.Assistant  assistant = assistantsRepository.findByCompany(companyId);
        if(assistant == null) {
            throw new RuntimeException("No assistant found for the given company and type.");
        }
        String systemPrompt = assistant.getSystemPrompt();
        ChatLanguageModel chatModel = buildChatModel(assistant);
        var response = chatModel.chat(ChatRequest.builder()
            .messages(
                new SystemMessage(systemPrompt),
                new UserMessage(message)
            )
            .build());
        String text = response.aiMessage() != null ? response.aiMessage().text() : null;
        System.out.println("Response from Gemini: " + text);
        return text;
        
    }
    @Override
    public ChatLanguageModel buildChatModel(Domain.Models.Assistant assistant) {
     
        String apiKey = assistant.getToken();
        String modelName = assistant.getModel();
        String baseUrl = assistant.getUrl();
        //double temperature = Double.parseDouble(assistantsRepository.getValueOrDefault("OPENAI_TEMPERATURE", "0.7"));
        //long timeoutSeconds = Long.parseLong(assistantsRepository.getValueOrDefault("OPENAI_TIMEOUT_SECONDS", "60"));

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName(modelName)
                // .temperature(temperature)
                // .timeout(Duration.ofSeconds(timeoutSeconds))
                .logRequests(true)
                .logResponses(true)
                .build();
    }
  
}
