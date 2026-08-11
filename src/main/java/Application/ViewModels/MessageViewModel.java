package Application.ViewModels;

import lombok.Data;
@Data
public class MessageViewModel {
    private Integer id;
    private String text;
    private Boolean isFromContact;
    private Boolean isFromCompany;
    private Integer companyId;
    private Integer contactId;
}
