package Application.Entities;

import lombok.Data;

@Data
public class SaveCompanyRequest {
    private String name;
    private String email;
}
