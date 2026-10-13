package Application.Entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class TestScriptRequest {
    private Object argsJson;
    private String script;

    public String getArgsJsonAsString() {
        if (argsJson == null) return "{}";
        if (argsJson instanceof String) return (String) argsJson;
        try {
            return new ObjectMapper().writeValueAsString(argsJson);
        } catch (Exception e) {
            return "{}";
        }
    }
}
