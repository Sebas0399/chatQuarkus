package Application.Contracts;

import Application.Entities.AuthRequest;
import Application.ViewModels.AuthViewModel;

public interface IAuthService {
    public AuthViewModel login(AuthRequest request);
}
