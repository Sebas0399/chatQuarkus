package Application.Entities;

import lombok.Data;

@Data
public class ContactFilter {
    private Integer companyId;
    private String search;
}
