package db;

import db.utils.JsonObjectConverter;
import jakarta.json.Json;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Table (name = "companies")
@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String webhookToken;
	@Column(columnDefinition = "jsonb") // o "json"
	@Convert(converter = JsonObjectConverter.class)
	private Json configs;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Assistant> assistans = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<User> users = new ArrayList<>();
}
