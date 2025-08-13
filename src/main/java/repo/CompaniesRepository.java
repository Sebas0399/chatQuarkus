package repo;

import db.Company;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class CompaniesRepository implements PanacheRepositoryBase<Company,Integer> {
}
