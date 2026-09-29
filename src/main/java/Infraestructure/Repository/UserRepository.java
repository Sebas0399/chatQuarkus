package Infraestructure.Repository;

import Domain.Contracts.IUserRepository;
import Infraestructure.Contracts.Entities.Company;
import Infraestructure.Contracts.Entities.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class UserRepository implements IUserRepository, PanacheRepository<User> {

    @Inject
    EntityManager entityManager;

    @Override
    public Domain.Models.User findByUsernameAndPassword(String username, String password) {
        User entity = find("username = ?1", username).firstResult();
        if (entity != null && BcryptUtil.matches(password, entity.getPassword())) {
            return toDomain(entity);
        }
        return null;
    }

    private Domain.Models.User toDomain(User entity) {
        Domain.Models.User user = new Domain.Models.User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        user.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        return user;
    }

    private User toInfraestrucrure(Domain.Models.User entity) {
        User user = new User();
        user.setUsername(entity.getUsername());
        user.setPassword(BcryptUtil.bcryptHash(entity.getPassword()));
        Company company = entityManager.getReference(
                Company.class,
                entity.getCompanyId());
        user.setCompany(company);

        return user;
    }

    @Override
    public Boolean saveUser(Domain.Models.User entity) {
        persist(toInfraestrucrure(entity));
        return true;
    }
}
