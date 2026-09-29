package Application.ViewModels;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContactViewModel {
    private Integer id;
    private String name;
    private String number;
    private Integer companyId;
    private String email;
    private LocalDateTime lastInteraction;
}
