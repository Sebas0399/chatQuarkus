package Domain.Models;

import lombok.Data;

@Data
public class Company {
    private Integer id;
    private String name;
    private String email;
    private String webhookToken;
}
