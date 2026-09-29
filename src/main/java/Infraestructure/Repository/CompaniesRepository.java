package Infraestructure.Repository;

import Domain.Contracts.ICompanyRepository;
import Infraestructure.Contracts.Entities.Company;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CompaniesRepository implements ICompanyRepository, PanacheRepository<Company> {

    @Override
    public Boolean saveCompany(Domain.Models.Company entity) {
        persist(toInfraestructure(entity));
        return true;
    }

    @Override
    public Domain.Models.Company findByWebhookToken(String token) {
        Company entity = find("webhookToken = ?1", token).firstResult();
        return toDomain(entity);
    }

    @Override
    public Domain.Models.Company getCompanyById(Integer id) {
        if (id == null) return null;
        Company entity = find("id = ?1", id).firstResult();
        return toDomain(entity);
    }

    private Company toInfraestructure(Domain.Models.Company entity) {
        Company company = new Company();
        company.setName(entity.getName());
        company.setEmail(entity.getEmail());
        company.setWebhookToken(entity.getWebhookToken());
        return company;
    }

    private Domain.Models.Company toDomain(Company entity) {
        if (entity == null) return null;
        Domain.Models.Company company = new Domain.Models.Company();
        company.setId(entity.getId());
        company.setName(entity.getName());
        company.setEmail(entity.getEmail());
        company.setWebhookToken(entity.getWebhookToken());
        return company;
    }
}
