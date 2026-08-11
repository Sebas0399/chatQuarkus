package Domain.Contracts;

import java.util.List;

public interface IMessageRepository {
    public List<Domain.Models.Message> findByContactId(Integer contactId);
}
