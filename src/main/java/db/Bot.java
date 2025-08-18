package db;

import db.utils.JsonObjectConverter;
import jakarta.json.Json;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "bots")
@Entity
@Data
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(columnDefinition = "jsonb") // o "json"
    @Convert(converter = JsonObjectConverter.class)
    private Json components;
    private String trigger;
    private Integer company_id;
    private Integer bot_type_id;
}
