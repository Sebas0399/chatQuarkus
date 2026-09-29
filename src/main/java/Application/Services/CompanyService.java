package Application.Services;

import Application.Contracts.ICompanyService;
import Application.Entities.SaveCompanyRequest;
import Application.Mappers.Contracts.ICompanyMapper;
import Domain.Contracts.ICompanyRepository;
import Domain.Models.Company;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CompanyService implements ICompanyService {
    @Inject
    ICompanyRepository companyRepository;
    @Inject
    ICompanyMapper mapper;

    @Override
    public Boolean saveCompany(SaveCompanyRequest request) {
        return companyRepository.saveCompany(mapper.toDomain(request));
    }

    @Override
    public Company findByWebhookToken(String token) {
        return companyRepository.findByWebhookToken(token);
    }

    @Override
    public Company getCompanyById(Integer id) {
        return companyRepository.getCompanyById(id);
    }
}
