package Application.Services;

import java.util.List;

import Application.Contracts.IMessageService;
import Application.Mappers.Contracts.IMessageMapper;
import Application.ViewModels.MessageViewModel;
import Domain.Contracts.IMessageRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped

public class MessageService implements IMessageService {

    @Inject
    IMessageRepository messageRepository;
    @Inject 
    IMessageMapper messageMapper;
    @Override
    public List<MessageViewModel> findByContactId(Integer companyId) {
        // TODO Auto-generated method stub
        return messageRepository.findByContactId(companyId).stream()
        .map(messageMapper::toViewModel)
        .toList();
    }

}
