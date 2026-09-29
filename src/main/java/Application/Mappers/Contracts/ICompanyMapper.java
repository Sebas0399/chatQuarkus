package Application.Mappers.Contracts;

import Application.Entities.SaveCompanyRequest;

public interface ICompanyMapper {
    public Domain.Models.Company toDomain(SaveCompanyRequest request);
}
