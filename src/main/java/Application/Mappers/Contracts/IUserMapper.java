package Application.Mappers.Contracts;

import Application.Entities.UserRegisterRequest;
import Application.ViewModels.MessageViewModel;
import Domain.Models.User;

public interface IUserMapper {
    public Domain.Models.User toDomain(UserRegisterRequest request);

}
