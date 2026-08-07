package Domain.Models;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private Integer companyId;
}
