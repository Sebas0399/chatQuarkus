package dtos;

import lombok.Data;

@Data
public class MessageDto {
    private Integer id;
    private String text;
    private Boolean isFromContact;
    private Boolean isFromCompany;
    private Integer company_id;
    private Integer contact_id;
}
