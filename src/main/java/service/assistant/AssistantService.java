package service.assistant;

import java.util.Optional;

import repo.AssistantsRepository;

@ApplicationScoped
public class AssistantService {
    @Inject 
    AssistantsRepository assistantsRepository;
    public Optional<IA_TYPE> getActiveIaProvider(Integer companyId){
        //
        var res=assistantsRepository.findByCompanyId(companyId);

    }
}
