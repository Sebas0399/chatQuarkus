package Application.Mappers.Contracts;

import Application.ViewModels.ContactViewModel;

public interface IContactMapper {
    public ContactViewModel toViewModel(Domain.Models.Contact contact);
}
