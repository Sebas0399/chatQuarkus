package Infraestructure.Repository;

import Domain.Contracts.IContactsRepository;
import Domain.Contracts.IUserRepository;
import Infraestructure.Contracts.Entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
@ApplicationScoped
@Transactional
public class UserRepository implements IUserRepository, PanacheRepository<User>  {

	@Override
	public Domain.Models.User findByUsernameAndPassword(String username, String password) {
		return toDomain(find("username = ?1 and password = ?2", username, password).firstResult());
              
	}
    private Domain.Models.User toDomain(User entity){
        Domain.Models.User user = new Domain.Models.User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());
        user.setCompanyId(entity.getCompany() != null ? entity.getCompany().getId() : null);
        return user;
    }
}
