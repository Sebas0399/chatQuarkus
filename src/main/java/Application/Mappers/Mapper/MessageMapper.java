package Application.Mappers.Mapper;

import Application.ViewModels.MessageViewModel;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped

public class MessageMapper implements Application.Mappers.Contracts.IMessageMapper {

    @Override
    public MessageViewModel toViewModel(Domain.Models.Message message) {
        MessageViewModel viewModel = new MessageViewModel();
        viewModel.setId(message.getId());
        viewModel.setText(message.getText());
        viewModel.setIsFromContact(message.getIsFromContact());
        viewModel.setIsFromCompany(message.getIsFromCompany());
        viewModel.setCompanyId(message.getCompanyId());
        viewModel.setContactId(message.getContactId());
        return viewModel;
    }
}
