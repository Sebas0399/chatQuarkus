package Application.Services;

import Application.Contracts.IAssistantService;
import Application.Entities.SaveAssistantRequest;
import Application.Mappers.Contracts.IAssistantMapper;
import Application.ViewModels.AssistantViewModel;
import Domain.Contracts.IAssistantRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AssistantService implements IAssistantService {
    @Inject
    IAssistantRepository assistantRepository;
    @Inject
    IAssistantMapper mapper;

    @Override
    public AssistantViewModel getAssistant(Integer companyId) {
        return mapper.toViewModel(assistantRepository.findByCompany(companyId));
    }

    @Override
    public Boolean saveAssistant(SaveAssistantRequest request) {
        return assistantRepository.saveAssistant(mapper.toDomain(request));
    }

}
