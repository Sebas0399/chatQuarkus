package Application.Services;

import org.eclipse.microprofile.jwt.JsonWebToken;

import Application.Contracts.IUserContextService;
import Domain.Models.DomainException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserContextService implements IUserContextService {

    @Inject
    JsonWebToken jwt;

    @Override
    public Integer getCompanyId() {
        if (jwt.getClaimNames() == null || !jwt.containsClaim("companyId")) {
            throw new DomainException("No se encontró la compañía en la sesión actual.");
        }
        Object claim = jwt.getClaim("companyId");
        return claim instanceof Number ? ((Number) claim).intValue() : Integer.parseInt(claim.toString());
    }

    @Override
    public String getUsername() {
        return jwt.getName();
    }
}
