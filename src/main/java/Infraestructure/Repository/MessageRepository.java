package Infraestructure.Repository;

import java.util.List;

import Domain.Contracts.IMessageRepository;
import Domain.Contracts.IUserRepository;
import Domain.Models.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class MessageRepository
        implements IMessageRepository, PanacheRepository<Infraestructure.Contracts.Entities.Message> {

    @Override
    public List<Message> findByContactId(Integer contactId) {
        return find("contact.id = ?1", contactId).stream()
                .map(this::toDomain)
                .toList();
    }

    private Domain.Models.Message toDomain(Infraestructure.Contracts.Entities.Message entity) {

        Domain.Models.Message message = new Domain.Models.Message();
        message.setId(entity.getId());
        message.setText(entity.getText());
        message.setIsFromContact(entity.getIsFromContact());
        message.setIsFromCompany(entity.getIsFromCompany());
        message.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        message.setContactId(entity.getContact() != null ? entity.getContact().getId() : null);
        return message;
    }
}
