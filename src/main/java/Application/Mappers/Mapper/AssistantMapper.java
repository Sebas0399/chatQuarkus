package Application.Mappers.Mapper;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Application.Entities.DocumentConfig;
import Application.Entities.SaveAssistantRequest;
import Application.Entities.ToolConfig;
import Application.Mappers.Contracts.IAssistantMapper;
import Application.ViewModels.AssistantViewModel;
import Domain.Models.Assistant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class AssistantMapper implements IAssistantMapper {

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public AssistantViewModel toViewModel(Assistant assistant) {
        if (assistant == null) return null;
        AssistantViewModel assistantViewModel = new AssistantViewModel();
        assistantViewModel.setCompanyId(assistant.getCompanyId());
        assistantViewModel.setIaProvider(assistant.getIaProvider());
        assistantViewModel.setId(assistant.getId());
        assistantViewModel.setModel(assistant.getModel());
        assistantViewModel.setSystemPrompt(assistant.getSystemPrompt());
        assistantViewModel.setToken(assistant.getToken());
        assistantViewModel.setUrl(assistant.getUrl());
        assistantViewModel.setTools(StrintToTools(assistant.getToolsJson()));
        assistantViewModel.setDocuments(StrintToDocumentConfig(assistant.getDocumentsJson()));
        return assistantViewModel;
    }

    private List<ToolConfig> StrintToTools(String tools) {
        if (tools == null || tools.isBlank())
            return Collections.emptyList();
        try {
            return objectMapper.readValue(tools, new TypeReference<List<ToolConfig>>() {
            });
        } catch (Exception e) {
            return Collections.emptyList();

        }
    }

    private List<DocumentConfig> StrintToDocumentConfig(String documents) {
        if (documents == null || documents.isBlank())
            return Collections.emptyList();
        try {
            return objectMapper.readValue(documents, new TypeReference<List<DocumentConfig>>() {
            });
        } catch (Exception e) {
            return Collections.emptyList();

        }
    }

    @Override
    public Assistant toDomain(SaveAssistantRequest request) {
        Assistant assistant =new Assistant();
        assistant.setCompanyId(request.getCompanyId());
        assistant.setDocumentsJson(request.getDocumentsJson());
        assistant.setIaProvider(request.getIaProvider());
        assistant.setModel(request.getModel());
        assistant.setSystemPrompt(request.getSystemPrompt());
        assistant.setToken(request.getToken());
        assistant.setToolsJson(request.getToolsJson());
        assistant.setUrl(request.getUrl());
        return assistant;
    }
    

}
