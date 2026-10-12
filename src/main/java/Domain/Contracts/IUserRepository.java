package Domain.Contracts;



// @ApplicationScoped
// @Transactional
// public class IUserRepository implements PanacheRepositoryBase<User,Integer>{

// }
public interface IUserRepository {
     public Domain.Models.User findByUsernameAndPassword(String username, String password);
     public Boolean saveUser(Domain.Models.User entity);
}
