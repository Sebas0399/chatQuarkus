package Application.Entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 15, message = "El nombre de usuario debe tener entre 3 y 15 caracteres")
    private String username;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
}
