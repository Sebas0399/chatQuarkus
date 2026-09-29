package Application.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveMessageRequest {
    private String text;
    private Integer companyId;
    private Integer contactId;
    private Boolean isFromContact;
    private Boolean isFromCompany;
}
