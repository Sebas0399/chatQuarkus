package db;

import jakarta.persistence.*;
import lombok.Data;

@Table (name = "companies")
@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String webhookToken;
}
