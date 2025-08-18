package db;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "bot_types")
@Entity
@Data
public class BotType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
