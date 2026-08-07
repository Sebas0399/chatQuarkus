package Domain.Contracts;

import Infraestructure.Contracts.Entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

// @ApplicationScoped
// @Transactional
// public class IUserRepository implements PanacheRepositoryBase<User,Integer>{

// }
public interface IUserRepository {
     public Domain.Models.User findByUsernameAndPassword(String username, String password);
}
