package Application.Mappers.Contracts;

import Application.ViewModels.MessageViewModel;

public interface IMessageMapper {
    public MessageViewModel toViewModel(Domain.Models.Message message);

}
