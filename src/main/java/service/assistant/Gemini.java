package service.assistant;

import db.Assistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repo.AssistantsRepository;

@ApplicationScoped
public class Gemini implements IAssistant {
    @Inject
    private AssistantsRepository assistantsRepository;
    @Override
    public String response(String message,Integer companyId) {
        Assistant assistant = assistantsRepository.findByCompanyAndType(companyId, IA_TYPE.GEMINI);
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
    public ChatLanguageModel buildChatModel(Assistant assistant) {
     
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
