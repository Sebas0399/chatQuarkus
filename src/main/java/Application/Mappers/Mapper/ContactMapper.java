package Application.Mappers.Mapper;

import Application.Mappers.Contracts.IContactMapper;
import Application.ViewModels.ContactViewModel;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactMapper implements IContactMapper {
    public ContactViewModel toViewModel(Domain.Models.Contact contact) {
        ContactViewModel viewModel = new ContactViewModel();
        viewModel.setId(contact.getId());
        viewModel.setName(contact.getName());
        viewModel.setCompanyId(contact.getCompanyId());
        return viewModel;
    }
}
