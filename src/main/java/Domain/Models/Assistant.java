package Domain.Models;
import lombok.Data;
import service.assistant.IA_TYPE;

@Data
public class Assistant {
    private Integer id;
    private IA_TYPE iaProvider;
    private String url;
    private String token;
    private String model;
    private String systemPrompt;
    private String toolsJson;
    private String documentsJson;
    private Integer companyId;
}
