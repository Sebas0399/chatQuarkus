package Application.Contracts;

import java.util.Set;

public interface IJwtService {
    public String generateToken(String username, Set<String> roles);
}
