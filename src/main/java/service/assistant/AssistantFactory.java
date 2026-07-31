package service.assistant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped

public class AssistantFactory {
    @Inject
    Gemini geminiService;

    public IAssistant getProvider(IA_TYPE type) {
        switch (type) {
            case GEMINI:
                return geminiService;
            default:
                break;
        }

        return null;
    }
}
