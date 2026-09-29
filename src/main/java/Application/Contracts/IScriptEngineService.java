package Application.Contracts;

import Application.Entities.TestToolResponse;

public interface IScriptEngineService {
    public TestToolResponse testScript(String jsCode, String jsonArguments);
    // public String runScript(String jsCode, String jsonArguments) throws Exception;
    
}
