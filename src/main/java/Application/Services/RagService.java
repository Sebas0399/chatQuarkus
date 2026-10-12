package Application.Services;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallenv15.BgeSmallEnV15EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RagService {

    private final EmbeddingModel embeddingModel;
    private final ElasticsearchEmbeddingStore embeddingStore;

    public RagService(
            @ConfigProperty(name = "elasticsearch.url", defaultValue = "http://localhost:9200") String elasticsearchUrl,
            @ConfigProperty(name = "elasticsearch.index", defaultValue = "chatquarks_rag_index") String elasticsearchIndex
    ) {
        this.embeddingModel = new BgeSmallEnV15EmbeddingModel();

        this.embeddingStore = ElasticsearchEmbeddingStore.builder()
                .serverUrl(elasticsearchUrl)
                .indexName(elasticsearchIndex)
                .dimension(512)
                .build();
    }

    public void ingestDocument(String documentText, String title, Integer companyId) {
        TextSegment segment = TextSegment.from(documentText);
        segment.metadata().put("companyId", String.valueOf(companyId));
        segment.metadata().put("title", title);

        Embedding embedding = embeddingModel.embed(segment).content();
        embeddingStore.add(embedding, segment);
    }

    public String retrieveRelevantContext(String query, Integer companyId) {
        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(0.6)
                .build();

        EmbeddingSearchResult<TextSegment> result = embeddingStore.search(request);

        List<String> relevantSegments = result.matches().stream()
                .map(EmbeddingMatch::embedded)
                .filter(segment -> {
                    String metaCompany = segment.metadata().get("companyId");
                    return metaCompany == null || metaCompany.equals(String.valueOf(companyId));
                })
                .map(TextSegment::text)
                .collect(Collectors.toList());

        if (relevantSegments.isEmpty()) {
            return "";
        }

        return String.join("\n\n", relevantSegments);
    }
}
