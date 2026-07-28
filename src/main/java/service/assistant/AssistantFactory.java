package service.assistant;

@ApplicationScoped

public class AssistantFactory {
    @Inject
    Gemini geminiService;

    public IAssistant getProvider(IA_TYPE type ){
        switch (type) {
            case IA_TYPE.GEMINI:
                return geminiService; 
                break;
        
            default:
                break;
        }
    }
}
