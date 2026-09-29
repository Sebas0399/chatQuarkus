package Application.Contracts;

import Application.Entities.ContactFilter;
import Application.Entities.PagedRequest;
import Application.ViewModels.ContactViewModel;
import Domain.Models.PageResponse;

public interface IContactService {
    PageResponse<ContactViewModel> findByCompanyId(PagedRequest<ContactFilter> request);

    Boolean updateContact(ContactViewModel contact);

    ContactViewModel findByCompanyAndId(Integer contactId, Integer companyId);

    ContactViewModel findByCompanyAndNumber(Integer companyId, String number);

    ContactViewModel createContact(ContactViewModel contact);

    ContactViewModel findOrCreateContact(Integer companyId, String number, String name);

    Boolean updateNotification(Integer contactId, boolean hasNotification);
}
