package Domain.Models;

import lombok.Data;

@Data
public class Message {
 private Integer id;
    private String text;
    private Boolean isFromContact;
    private Boolean isFromCompany;
    private Integer companyId;
    private Integer contactId;
}
