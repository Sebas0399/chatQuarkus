package Application.Mappers.Mapper;

import Application.Entities.UserRegisterRequest;
import Application.Mappers.Contracts.IUserMapper;
import Domain.Models.User;
import jakarta.enterprise.context.ApplicationScoped;
@ApplicationScoped
public class UserMapper implements IUserMapper {

    @Override
    public User toDomain(UserRegisterRequest request) {
        User user = new User();
        user.setCompanyId(request.getCompanyId());
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        return user;
    }

}
