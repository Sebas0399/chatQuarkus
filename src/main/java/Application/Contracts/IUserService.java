package Application.Contracts;

import Application.Entities.UserRegisterRequest;

public interface IUserService {
    public Boolean saveUser(UserRegisterRequest request);

}
