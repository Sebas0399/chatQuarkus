package Application.Services;

import Application.Contracts.IUserService;
import Application.Entities.UserRegisterRequest;
import Application.Mappers.Contracts.IUserMapper;
import Domain.Contracts.IUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class UserService implements IUserService {
    @Inject
    IUserRepository userRepository;
    @Inject
    IUserMapper userMapper;

    @Override
    public Boolean saveUser(UserRegisterRequest request) {
        userRepository.saveUser(userMapper.toDomain(request));
        return true;
    }

}
