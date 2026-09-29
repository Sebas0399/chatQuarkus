package Application.Entities;

import lombok.Data;

@Data
public class TestScriptRequest {
    private String argsJson;
    private String script;
}
