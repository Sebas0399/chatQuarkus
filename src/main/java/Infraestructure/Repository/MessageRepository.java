package Infraestructure.Repository;

import java.util.List;
import Domain.Contracts.IMessageRepository;
import Domain.Models.Message;
import Infraestructure.Contracts.Entities.Company;
import Infraestructure.Contracts.Entities.Contact;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class MessageRepository
        implements IMessageRepository, PanacheRepository<Infraestructure.Contracts.Entities.Message> {
    @Inject
    EntityManager entityManager;

    @Override
    public List<Message> findByContactId(Integer contactId) {
        return find("contact.id = ?1", contactId).stream()
                .map(this::toDomain)
                .toList();
    }

    private Domain.Models.Message toDomain(Infraestructure.Contracts.Entities.Message entity) {
        if (entity == null) return null;
        Domain.Models.Message message = new Domain.Models.Message();
        message.setId(entity.getId());
        message.setText(entity.getText());
        message.setIsFromContact(entity.getIsFromContact());
        message.setIsFromCompany(entity.getIsFromCompany());
        message.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        message.setContactId(entity.getContact() != null ? entity.getContact().getId() : null);
        return message;
    }

    private Infraestructure.Contracts.Entities.Message toInfraestructure(Domain.Models.Message entity) {
        if (entity == null) return null;
        Infraestructure.Contracts.Entities.Message message = new Infraestructure.Contracts.Entities.Message();
        message.setIsFromCompany(entity.getIsFromCompany());
        message.setIsFromContact(entity.getIsFromContact());
        if (entity.getCompanyId() != null) {
            Company company = entityManager.getReference(
                    Company.class,
                    entity.getCompanyId());
            message.setCompany(company);
        }
        if (entity.getContactId() != null) {
            Contact contact = entityManager.getReference(
                    Contact.class,
                    entity.getContactId());
            message.setContact(contact);
        }
        message.setText(entity.getText());
        return message;
    }

    @Override
    public Boolean saveMessage(Message entity) {
        persist(toInfraestructure(entity));
        return true;
    }
}
