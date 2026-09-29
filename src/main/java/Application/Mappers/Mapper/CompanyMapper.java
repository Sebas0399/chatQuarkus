package Application.Mappers.Mapper;

import Application.Entities.SaveCompanyRequest;
import Application.Mappers.Contracts.ICompanyMapper;
import Domain.Models.Company;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class CompanyMapper implements ICompanyMapper {

    @Override
    public Company toDomain(SaveCompanyRequest request) {
        // TODO Auto-generated method stub
        Company company= new Company();
        company.setEmail(request.getEmail());
        company.setName(request.getName());
        return company;
    }

}
