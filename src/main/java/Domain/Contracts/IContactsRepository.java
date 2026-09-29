package Domain.Contracts;

import java.util.List;

import Domain.Models.Contact;
import io.smallrye.mutiny.tuples.Tuple4;

public interface IContactsRepository {
    Tuple4<List<Contact>, Integer, Integer, Long> findByCompanyId(Integer companyId, Integer pagina, Integer size);

    Contact getContact(Integer contactId);

    Contact findByCompanyAndId(Integer contactId, Integer companyId);

    Contact findByCompanyAndNumber(Integer companyId, String number);

    Contact createContact(Contact contact);

    Boolean updateContact(Contact contact);

    Boolean updateNotification(Integer contactId, boolean hasNotification);
}
