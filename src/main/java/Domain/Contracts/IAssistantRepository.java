package Domain.Contracts;

import Domain.Models.Assistant;

public interface IAssistantRepository {
    public Assistant findByCompany(Integer companyId);
    public Boolean saveAssistant(Assistant assistant);
}
