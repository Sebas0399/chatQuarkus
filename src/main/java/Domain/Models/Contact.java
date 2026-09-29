package Domain.Models;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Contact {
    private Integer id;
    private String name;
    private String number;
    private Integer companyId;
    private String email;
    private LocalDateTime lastInteraction;
}
