package Application.Mappers.Contracts;

import Application.Entities.SaveMessageRequest;
import Application.ViewModels.MessageViewModel;
import Domain.Models.Message;

public interface IMessageMapper {
    public MessageViewModel toViewModel(Domain.Models.Message message);
    public Message toDomain (SaveMessageRequest request);

}
