package Application.Mappers.Contracts;

import Application.Entities.SaveAssistantRequest;
import Application.ViewModels.AssistantViewModel;
import Application.ViewModels.MessageViewModel;
import Domain.Models.Assistant;

public interface IAssistantMapper {
    public AssistantViewModel toViewModel(Domain.Models.Assistant assistant);
    public Assistant toDomain(SaveAssistantRequest request);
    
}
