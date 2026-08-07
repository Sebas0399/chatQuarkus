package Domain.Contracts;

import java.util.List;

import Domain.Models.Contact;
public interface IContactsRepository {
     public List<Contact> findByCompanyId(Integer companyId);
}
