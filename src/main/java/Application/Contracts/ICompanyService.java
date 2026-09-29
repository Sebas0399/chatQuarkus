package Application.Contracts;

import Application.Entities.SaveCompanyRequest;
import Domain.Models.Company;

public interface ICompanyService {
    Boolean saveCompany(SaveCompanyRequest request);
    Company findByWebhookToken(String token);
    Company getCompanyById(Integer id);
}
