package Infraestructure.Repository;

import Domain.Contracts.IAssistantRepository;
import Infraestructure.Contracts.Entities.Assistant;
import Infraestructure.Contracts.Entities.Company;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class AssistantsRepository implements IAssistantRepository, PanacheRepository<Assistant> {
    @Inject
    EntityManager entityManager;

    public Domain.Models.Assistant findByCompany(Integer companyId) {
        Assistant entity = find("company.id = ?1", companyId).firstResult();
        return toDomain(entity);
    }

    private Domain.Models.Assistant toDomain(Assistant entity) {
        if (entity == null) return null;
        Domain.Models.Assistant assistant = new Domain.Models.Assistant();
        assistant.setDocumentsJson(entity.getDocumentsJson());
        assistant.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        assistant.setIaProvider(entity.getIaProvider());
        assistant.setId(entity.getId());
        assistant.setModel(entity.getModel());
        assistant.setSystemPrompt(entity.getSystemPrompt());
        assistant.setToken(entity.getToken());
        assistant.setUrl(entity.getUrl());
        return assistant;
    }

    private Infraestructure.Contracts.Entities.Assistant toInfraestructure(Domain.Models.Assistant entity) {
        Infraestructure.Contracts.Entities.Assistant assistant = new Assistant();
        Company company = entityManager.getReference(
                Company.class,
                entity.getCompanyId());
        assistant.setCompany(company);
        assistant.setDocumentsJson(entity.getDocumentsJson());
        assistant.setIaProvider(entity.getIaProvider());
        assistant.setModel(entity.getModel());
        assistant.setSystemPrompt(entity.getSystemPrompt());
        assistant.setToken(entity.getToken());
        assistant.setToolsJson(entity.getToolsJson());
        assistant.setUrl(entity.getUrl());
        return assistant;
    }

    @Override
    public Boolean saveAssistant(Domain.Models.Assistant assistant) {
        persist(toInfraestructure(assistant));
        return true;
    }
}
