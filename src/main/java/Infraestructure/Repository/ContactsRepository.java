package Infraestructure.Repository;

import java.util.List;

import Domain.Contracts.IContactsRepository;
import Infraestructure.Contracts.Entities.Contact;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ContactsRepository implements IContactsRepository, PanacheRepository<Contact> {

    @Override
    public List<Domain.Models.Contact> findByCompanyId(Integer companyId) {
        // TODO Auto-generated method stub
        return find("company_id", companyId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Domain.Models.Contact toDomain(Contact entity) {
        Domain.Models.Contact contact = new Domain.Models.Contact();
        contact.setId(entity.getId());
        contact.setName(entity.getName());
        contact.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        return contact;
    }
}
