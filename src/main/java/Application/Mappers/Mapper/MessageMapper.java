package Application.Mappers.Mapper;

import Application.Entities.SaveMessageRequest;
import Application.ViewModels.MessageViewModel;
import Domain.Models.Message;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageMapper implements Application.Mappers.Contracts.IMessageMapper {

    @Override
    public MessageViewModel toViewModel(Domain.Models.Message message) {
        if (message == null) return null;
        MessageViewModel viewModel = new MessageViewModel();
        viewModel.setId(message.getId());
        viewModel.setText(message.getText());
        viewModel.setIsFromContact(message.getIsFromContact());
        viewModel.setIsFromCompany(message.getIsFromCompany());
        viewModel.setCompanyId(message.getCompanyId());
        viewModel.setContactId(message.getContactId());
        return viewModel;
    }

    @Override
    public Message toDomain(SaveMessageRequest request) {
        if (request == null) return null;
        Message message = new Message();
        message.setCompanyId(request.getCompanyId());
        message.setContactId(request.getContactId());
        message.setText(request.getText());
        message.setIsFromContact(request.getIsFromContact() != null ? request.getIsFromContact() : false);
        message.setIsFromCompany(request.getIsFromCompany() != null ? request.getIsFromCompany() : true);
        return message;
    }
}
