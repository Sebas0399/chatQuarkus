package Application.ViewModels;

import java.util.List;

import Application.Entities.DocumentConfig;
import Application.Entities.ToolConfig;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import service.assistant.IA_TYPE;

@Data
public class AssistantViewModel {
    private Integer id;
    private IA_TYPE iaProvider;
    private String url;
    private String token;
    private String model;
    private String systemPrompt;
    private Integer companyId;
    private List<ToolConfig> tools;
    private List<DocumentConfig> documents;
}
