package Application.Services;

import java.time.LocalDateTime;
import java.util.List;

import Application.Contracts.IContactService;
import Application.Entities.ContactFilter;
import Application.Entities.PagedRequest;
import Application.Mappers.Contracts.IContactMapper;
import Application.ViewModels.ContactViewModel;
import Domain.Contracts.IContactsRepository;
import Domain.Models.Contact;
import Domain.Models.PageResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactService implements IContactService {
  @Inject
  IContactsRepository contactsRepository;
  @Inject
  IContactMapper contactMapper;

  @Override
  public PageResponse<ContactViewModel> findByCompanyId(PagedRequest<ContactFilter> request) {
    Integer companyId = request.getFilter() != null ? request.getFilter().getCompanyId() : null;
    var result = contactsRepository.findByCompanyId(companyId, request.getPage(), request.getSize());
    List<ContactViewModel> viewModels = result.getItem1().stream()
        .map(contactMapper::toViewModel)
        .toList();
    return PageResponse.of(viewModels, result.getItem2(), result.getItem3(), result.getItem4());
  }

  @Override
  public Boolean updateContact(ContactViewModel contact) {
    return contactsRepository.updateContact(contactMapper.toDomain(contact));
  }

  @Override
  public ContactViewModel findByCompanyAndId(Integer contactId, Integer companyId) {
    return contactMapper.toViewModel(contactsRepository.findByCompanyAndId(contactId, companyId));
  }

  @Override
  public ContactViewModel findByCompanyAndNumber(Integer companyId, String number) {
    var contact = contactsRepository.findByCompanyAndNumber(companyId, number);
    return contact != null ? contactMapper.toViewModel(contact) : null;
  }

  @Override
  public ContactViewModel createContact(ContactViewModel contact) {
    var domain = contactMapper.toDomain(contact);
    var created = contactsRepository.createContact(domain);
    return contactMapper.toViewModel(created);
  }

  @Override
  public ContactViewModel findOrCreateContact(Integer companyId, String number, String name) {
    var existing = contactsRepository.findByCompanyAndNumber(companyId, number);
    if (existing != null) {
      return contactMapper.toViewModel(existing);
    }
    Contact newContact = new Contact();
    newContact.setCompanyId(companyId);
    newContact.setNumber(number);
    newContact.setName(name);
    newContact.setLastInteraction(LocalDateTime.now());
    var created = contactsRepository.createContact(newContact);
    return contactMapper.toViewModel(created);
  }

  @Override
  public Boolean updateNotification(Integer contactId, boolean hasNotification) {
    return contactsRepository.updateNotification(contactId, hasNotification);
  }
}
