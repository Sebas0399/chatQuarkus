package Application.Entities;

public record DocumentConfig(
        Long id,
        String fileName,
        String fileUrl,
        String status) {
}