package Application.Services;

import java.time.LocalDateTime;
import java.util.List;

import Application.Contracts.IMessageService;
import Application.Mappers.Contracts.IMessageMapper;
import Application.ViewModels.MessageViewModel;
import Domain.Contracts.IContactsRepository;
import Domain.Contracts.IMessageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped

public class MessageService implements IMessageService {

    @Inject
    IMessageRepository messageRepository;
    @Inject
    IMessageMapper messageMapper;
    @Inject
    IContactsRepository contactsRepository;

    @Override
    public List<MessageViewModel> findByContactId(Integer companyId) {
        // TODO Auto-generated method stub
        return messageRepository.findByContactId(companyId).stream()
                .map(messageMapper::toViewModel)
                .toList();
    }

    public Boolean saveMessage(Application.Entities.SaveMessageRequest request) {
        //actualizamos la ultima interaccion del contacto
        var contact=contactsRepository.getContact(request.getContactId());
        contact.setLastInteraction(LocalDateTime.now());
        contactsRepository.updateContact(contact);
        return messageRepository.saveMessage(messageMapper.toDomain(request));
    }

}
