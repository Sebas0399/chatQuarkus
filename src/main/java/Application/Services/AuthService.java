package Application.Services;

import Application.Contracts.IAuthService;
import Application.Entities.AuthRequest;
import Application.ViewModels.AuthViewModel;
import Domain.Contracts.IUserRepository;
import jakarta.inject.Inject;

public class AuthService implements IAuthService {
    @Inject
    IUserRepository userRepository;
    @Inject 
    IJwtService jwtService;

    @Override
    public AuthViewModel login(AuthRequest request);

    {
        var user=userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());
        var token = JwtService.generateToken(user.getUsername(), Set.of(user.getRole()));
        AuthViewModel res=new AuthViewModel;
        // Implement your login logic here
        // For example, you can validate the username and password against a database
        // and return an AuthViewModel with a token and role if successful.
        return ; // Replace with actual implementation
    }

}
