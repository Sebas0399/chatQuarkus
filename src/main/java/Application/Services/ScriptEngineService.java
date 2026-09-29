package Application.Services;

import Application.Contracts.IScriptEngineService;
import Application.Entities.TestToolResponse;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ScriptEngineService implements IScriptEngineService {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static class HttpBridge {
        @HostAccess.Export
        public String get(String url) throws Exception {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(15))
                    .header("User-Agent", "ChatNuxt-AI-Agent/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
    }

    // Receptor que captura el valor resuelto de las funciones async/await
    public static class AsyncResultReceiver {
        private String jsonResult = null;
        private String error = null;

        @HostAccess.Export
        public void resolve(String json) {
            this.jsonResult = json;
        }

        @HostAccess.Export
        public void reject(String err) {
            this.error = err;
        }

        public String getJsonResult() {
            return jsonResult;
        }

        public String getError() {
            return error;
        }
    }

    public TestToolResponse testScript(String jsCode, String jsonArguments) {
        if (jsCode == null || jsCode.isBlank()) {
            return TestToolResponse.fail("El script no puede estar vacío", 1, Collections.emptyList());
        }

        String safeArgs = (jsonArguments == null || jsonArguments.isBlank()) ? "{}" : jsonArguments;
        ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        AsyncResultReceiver receiver = new AsyncResultReceiver();

        try (Context context = Context.newBuilder("js")
                .allowAllAccess(false)
                .allowHostAccess(HostAccess.EXPLICIT)
                .out(logStream)
                .err(logStream)
                .option("engine.WarnInterpreterOnly", "false")
                .build()) {

            // 1. Inyectar HttpBridge, el receptor y polyfills de fetch y console.log
            context.getBindings("js").putMember("__httpBridge", new HttpBridge());
            context.getBindings("js").putMember("__receiver", receiver);

            context.eval("js",
                    """
                                // Polyfill de console.log para mostrar objetos JSON en los logs
                                const __originalLog = console.log;
                                console.log = function(...args) {
                                    const formatted = args.map(a => typeof a === 'object' && a !== null ? JSON.stringify(a) : a).join(' ');
                                    __originalLog(formatted);
                                };

                                // Polyfill de fetch con soporte JSON
                                const fetch = async function(url) {
                                    const rawBody = __httpBridge.get(url);
                                    return {
                                        ok: true,
                                        status: 200,
                                        text: async () => rawBody,
                                        json: async () => JSON.parse(rawBody)
                                    };
                                };
                            """);

            // 2. Evaluar el script del usuario
            Source source = Source.newBuilder("js", jsCode, "user_script.js").build();
            context.eval(source);

            // 3. Pasar argumentos JSON
            Value jsonParse = context.eval("js", "JSON.parse");
            Value jsArgs = jsonParse.execute(safeArgs);
            context.getBindings("js").putMember("__args", jsArgs);

            // 4. Ejecutar esperando la resolución de la Promise y enviar el resultado
            // resuelto a Java
            context.eval("js", """
                        (async () => {
                            try {
                                if (typeof execute !== 'function') {
                                    throw new Error("Debe existir la función 'execute(args)'");
                                }
                                const result = await execute(__args);
                                const stringified = JSON.stringify(result);
                                __receiver.resolve(stringified !== undefined ? stringified : "null");
                            } catch(err) {
                                __receiver.reject(err ? err.toString() : "Error en execute");
                            }
                        })();
                    """);

            if (receiver.getError() != null) {
                return TestToolResponse.fail(receiver.getError(), null, extractLogs(logStream));
            }

            String output = receiver.getJsonResult();
            if (output == null) {
                output = "{}";
            }

            return TestToolResponse.ok(output, extractLogs(logStream));

        } catch (PolyglotException e) {
            Integer line = (e.getSourceLocation() != null)
                    ? e.getSourceLocation().getStartLine()
                    : null;

            String cleanError = e.getMessage()
                    .replace("org.graalvm.polyglot.PolyglotException: ", "")
                    .replace("user_script.js:", "Línea ");

            return TestToolResponse.fail(cleanError, line, extractLogs(logStream));
        } catch (Exception e) {
            return TestToolResponse.fail("Error: " + e.getMessage(), null, extractLogs(logStream));
        }
    }

    private List<String> extractLogs(ByteArrayOutputStream stream) {
        String raw = stream.toString(StandardCharsets.UTF_8).trim();
        if (raw.isEmpty())
            return Collections.emptyList();
        return Arrays.asList(raw.split("\r?\n"));
    }
}