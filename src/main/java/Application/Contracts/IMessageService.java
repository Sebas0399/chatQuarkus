package Application.Contracts;

import java.util.List;

import Application.Entities.SaveMessageRequest;
import Application.ViewModels.MessageViewModel;

public interface IMessageService {
    public List<MessageViewModel> findByContactId(Integer contactId);
    public Boolean saveMessage(SaveMessageRequest request);

}
