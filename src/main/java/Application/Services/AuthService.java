package Application.Services;

import java.util.Set;

import Application.Contracts.IAuthService;
import Application.Contracts.IJwtService;
import Application.Entities.AuthRequest;
import Application.ViewModels.AuthViewModel;
import Domain.Contracts.IUserRepository;
import Domain.Models.DomainException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped

public class AuthService implements IAuthService {
    @Inject
    IUserRepository userRepository;
    @Inject
    IJwtService jwtService;

    @Override
    public AuthViewModel login(AuthRequest request)
    {
        var user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (user == null) {
            throw new DomainException("Credenciales inválidas");
        }
        
        var token = jwtService.generateToken(user.getUsername(), Set.of(user.getRole()),user.getCompanyId());
        AuthViewModel res = new AuthViewModel();
        res.setToken(token);
        res.setRole(user.getRole());
        res.setCompanyId(user.getCompanyId());
        return res;
    }

}
