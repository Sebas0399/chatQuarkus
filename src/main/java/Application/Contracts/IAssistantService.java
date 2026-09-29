package Application.Contracts;

import Application.Entities.SaveAssistantRequest;
import Application.ViewModels.AssistantViewModel;

public interface IAssistantService {
    public AssistantViewModel getAssistant(Integer companyId);
    public Boolean saveAssistant(SaveAssistantRequest request);
}       
