package Domain.Models;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private boolean success;
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;
    private T data;
    private Object errors; // Permite incluir listas de errores de validación, mapeos, etc.

    // --- Métodos de respuesta exitosa ---

    public static <T> Response<T> success(T data, String message) {
        return Response.<T>builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static <T> Response<T> success(T data) {
        return success(data, "Operación realizada con éxito");
    }

    // --- Métodos de respuesta de error ---

    public static <T> Response<T> error(String message, int statusCode) {
        return Response.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
    }

    public static <T> Response<T> error(String message, int statusCode, Object errors) {
        return Response.<T>builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .data(null)
                .build();
    }
}
