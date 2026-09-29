package Infraestructure.Contracts.Entities;

import service.assistant.IA_TYPE;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Table(name = "assistans")
@Entity
@Data
public class Assistant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.ORDINAL)
    private IA_TYPE iaProvider;
    private String url;
    private String token;
    private String model;
    private String systemPrompt;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    public String toolsJson; // JSON con las herramientas habilitadas
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    public String documentsJson; // JSON con los documentos indexados (RAG)
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading prevents performance issues
    @JoinColumn(name = "company_id")
    public Company company;
}
