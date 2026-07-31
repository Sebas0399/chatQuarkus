package repo;

import db.Contact;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class ContactsRepository implements PanacheRepositoryBase<Contact,Integer> {
    public List<Contact> findByCompanyId(Integer companyId){
        return find("company_id",companyId).stream().toList();
    }
    public Contact findByNumber(String number){
        return find("number",number).firstResult();
    }

}
