package Application.Contracts;

import java.util.List;

import Application.ViewModels.MessageViewModel;

public interface IMessageService {
    public List<MessageViewModel> findByContactId(Integer contactId);

}
