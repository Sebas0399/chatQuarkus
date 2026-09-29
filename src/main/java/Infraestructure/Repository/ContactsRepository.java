package Infraestructure.Repository;

import java.time.LocalDateTime;
import java.util.List;
import Domain.Contracts.IContactsRepository;
import Infraestructure.Contracts.Entities.Company;
import Infraestructure.Contracts.Entities.Contact;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.tuples.Tuple4;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ContactsRepository implements IContactsRepository, PanacheRepositoryBase<Contact, Integer> {

    @Inject
    EntityManager entityManager;

    @Override
    public Tuple4<List<Domain.Models.Contact>, Integer, Integer, Long> findByCompanyId(Integer companyId, Integer page,
            Integer size) {
        var query = find("company.id = ?1", companyId);
        Long total = query.count();
        var entities = query.page(page, size);
        var content = entities.stream().map(this::toDomain)
                .toList();
        return Tuple4.of(content, page, size, total);
    }

    private Domain.Models.Contact toDomain(Contact entity) {
        if (entity == null) return null;
        Domain.Models.Contact contact = new Domain.Models.Contact();
        contact.setId(entity.getId());
        contact.setName(entity.getName());
        contact.setNumber(entity.getNumber());
        contact.setEmail(entity.getEmail());
        contact.setLastInteraction(entity.getLastInteraction());
        contact.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        return contact;
    }

    @Override
    public Boolean updateContact(Domain.Models.Contact contact) {
        var contactFind = findById(contact.getId());
        if (contactFind == null) return false;
        contactFind.setEmail(contact.getEmail());
        contactFind.setName(contact.getName());
        contactFind.setNumber(contact.getNumber());
        contactFind.setLastInteraction(contact.getLastInteraction());
        return true;
    }

    @Override
    public Boolean updateNotification(Integer contactId, boolean hasNotification) {
        var contactFind = findById(contactId);
        if (contactFind == null) return false;
        contactFind.setHasNotification(hasNotification);
        return true;
    }

    @Override
    public Domain.Models.Contact getContact(Integer contactId) {
        var contact = findById(contactId);
        return toDomain(contact);
    }

    @Override
    public Domain.Models.Contact findByCompanyAndId(Integer contactId, Integer companyId) {
        Contact entity = find("id = ?1 and company.id = ?2", contactId, companyId).firstResult();
        return toDomain(entity);
    }

    @Override
    public Domain.Models.Contact findByCompanyAndNumber(Integer companyId, String number) {
        Contact entity = find("company.id = ?1 and number = ?2", companyId, number).firstResult();
        return toDomain(entity);
    }

    @Override
    public Domain.Models.Contact createContact(Domain.Models.Contact contact) {
        Contact entity = new Contact();
        entity.setName(contact.getName());
        entity.setNumber(contact.getNumber());
        entity.setEmail(contact.getEmail());
        entity.setLastInteraction(contact.getLastInteraction() != null ? contact.getLastInteraction() : LocalDateTime.now());
        entity.setHasNotification(true);
        if (contact.getCompanyId() != null) {
            Company company = entityManager.getReference(Company.class, contact.getCompanyId());
            entity.setCompany(company);
        }
        persist(entity);
        return toDomain(entity);
    }
}
