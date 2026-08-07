package Application.Contracts;

import java.util.List;

import Application.ViewModels.ContactViewModel;

public interface IContactService {
    public List<ContactViewModel> findByCompanyId(Integer companyId);
}
