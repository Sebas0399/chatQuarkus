package db;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;

@Entity
@Table(name = "users")
@UserDefinition
@Data

public class User {
    @Username
    private String username;
    @Password
    private String password;
    @Roles
    private String role;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading prevents performance issues
    @JoinColumn(name = "company_id")
    private Company company;
}
