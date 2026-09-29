package Domain.Contracts;

import Domain.Models.Company;

public interface ICompanyRepository {
    Boolean saveCompany(Company entity);
    Company findByWebhookToken(String token);
    Company getCompanyById(Integer id);
}
