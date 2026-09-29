package Application.Entities;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String username;
    private String password;
    private Integer companyId;
}
