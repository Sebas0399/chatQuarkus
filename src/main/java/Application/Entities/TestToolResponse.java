package Application.Entities;

import java.util.List;

public record TestToolResponse(
    boolean success,
    String output,
    String error,
    Integer line,
    List<String> logs
) {
    public static TestToolResponse ok(String output, List<String> logs) {
        return new TestToolResponse(true, output, null, null, logs);
    }

    public static TestToolResponse fail(String error, Integer line, List<String> logs) {
        return new TestToolResponse(false, null, error, line, logs);
    }
}