package repo;

import db.Company;
import db.Contact;
import db.Message;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class MessagesRepository implements PanacheRepositoryBase<Message,Integer> {
    public List<Message> findByContactId(Integer contactId){
        return find("contact_id",contactId).stream().toList();
    }
}
