package Application.Services;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

import Application.Contracts.IJwtService;

@ApplicationScoped
public class JwtService implements IJwtService {
	public String generateToken(String username, Set<String> roles) {
		return Jwt.issuer("quarkus-jwt")
			.upn(username)
			.groups(roles)
			.sign();
	}
}
