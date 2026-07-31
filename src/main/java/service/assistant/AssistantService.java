package service.assistant;

import java.util.Optional;

import db.Assistant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repo.AssistantsRepository;

@ApplicationScoped
public class AssistantService {
    @Inject 
    AssistantsRepository assistantsRepository;
    public Optional<IA_TYPE> getActiveIaProvider(Integer companyId){
        var res = assistantsRepository.find("company.id", companyId).firstResult();
        if (res == null) {
            return Optional.empty();
        }
        return Optional.of(((Assistant) res).getIaProvider());
    }
}
