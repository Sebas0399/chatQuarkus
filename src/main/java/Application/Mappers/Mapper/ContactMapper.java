package Application.Mappers.Mapper;

import Application.Mappers.Contracts.IContactMapper;
import Application.ViewModels.ContactViewModel;
import Domain.Models.Contact;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ContactMapper implements IContactMapper {
    public ContactViewModel toViewModel(Domain.Models.Contact contact) {
        ContactViewModel viewModel = new ContactViewModel();
        viewModel.setId(contact.getId());
        viewModel.setName(contact.getName());
        viewModel.setNumber(contact.getNumber());
        viewModel.setCompanyId(contact.getCompanyId());
        viewModel.setLastInteraction(contact.getLastInteraction());
        viewModel.setEmail(contact.getEmail());
        return viewModel;
    }

    @Override
    public Contact toDomain(ContactViewModel contact) {
        if (contact == null) return null;
        Contact contactDomain = new Contact();
        contactDomain.setId(contact.getId());
        contactDomain.setEmail(contact.getEmail());
        contactDomain.setCompanyId(contact.getCompanyId());
        contactDomain.setLastInteraction(contact.getLastInteraction());
        contactDomain.setName(contact.getName());
        contactDomain.setNumber(contact.getNumber());
        return contactDomain;
    }
}
