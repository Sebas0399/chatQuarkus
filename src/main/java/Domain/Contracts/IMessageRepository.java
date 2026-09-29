package Domain.Contracts;

import java.util.List;

import Domain.Models.Message;

public interface IMessageRepository {
    public List<Domain.Models.Message> findByContactId(Integer contactId);
    public Boolean saveMessage(Message entity);
}
