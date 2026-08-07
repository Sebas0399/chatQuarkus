package Infraestructure.Contracts.Entities;

import java.util.ArrayList;
import java.util.List;

import db.Company;
import db.Message;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    public Company company;
    private String number;
    private boolean hasNotification;
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Message> messages = new ArrayList<>();
}
