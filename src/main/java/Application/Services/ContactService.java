package Application.Services;

import java.util.List;

import Application.Contracts.IContactMapper;
import Application.ViewModels.ContactViewModel;
import Domain.Contracts.IContactsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ContactService {
  @Inject
  IContactsRepository contactsRepository;
  @Inject
  IContactMapper contactMapper;

  // devolvemos un viewModel
  public List<ContactViewModel> findByCompanyId(Integer companyId) {
    return contactsRepository.findByCompanyId(companyId).stream()
        .map(contactMapper::toViewModel)
        .collect(java.util.stream.Collectors.toList());
  }
}
