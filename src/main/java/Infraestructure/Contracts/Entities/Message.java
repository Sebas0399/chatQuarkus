package Infraestructure.Contracts.Entities;

import db.Company;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "messages")
@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private Boolean isFromContact;
    private Boolean isFromCompany;
    // private Integer company_id;
    // private Integer contact_id;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading prevents performance issues
    @JoinColumn(name = "company_id")
    public Company company;
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading prevents performance issues
    @JoinColumn(name = "contact_id")
    public Contact contact;
}
