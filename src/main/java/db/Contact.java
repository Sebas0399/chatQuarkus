package db;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "contacts")
@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer company_id;
    private String number;
}
