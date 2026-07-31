package repo;

import db.Assistant;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import service.assistant.IA_TYPE;

@ApplicationScoped
@Transactional
public class AssistantsRepository implements PanacheRepositoryBase<Assistant,Integer>{
    public Assistant findByCompanyAndType(Integer companyId, IA_TYPE type) {
        return find("company.id AND iaProvider", companyId, type).firstResult();
    }
}
