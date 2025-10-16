package in.ranjitha.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import in.ranjitha.model.AnswerResponse;
import in.ranjitha.model.DocumentMetadata;
import in.ranjitha.model.QuestionRequest;
import org.springframework.stereotype.Service;

import java.util.*;
        import java.util.stream.Collectors;

@Service
public class RagService {
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final List<DocumentMetadata> documents;
    private final OpenAiChatModel openAiChatModel;

    public RagService(OpenAiChatModel openAiChatModel) {
        this.embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        this.embeddingStore = new InMemoryEmbeddingStore<>();
        this.documents = new ArrayList<>();
        this.openAiChatModel= openAiChatModel;

    }

    /* Store document text and generate embeddings */
    public DocumentMetadata ingestDocument(String filename, String content) {
        // Split content into paragraphs or chunks (simple example: 500 chars per chunk)
        int chunkSize = 500;
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < content.length(); i += chunkSize) {
            int end = Math.min(content.length(), i + chunkSize);
            chunks.add(content.substring(i, end));
        }

        // Embed each chunk separately
        for (String chunk : chunks) {
            TextSegment segment = TextSegment.from(chunk);
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);
        }

        return new DocumentMetadata(filename, String.valueOf(content.length()));
    }


    /* Perform semantic search for a user question */
    public AnswerResponse answerQuestion(QuestionRequest questionRequest) {
        String question = questionRequest.getQuestion();

        Embedding queryEmbedding = embeddingModel.embed(question).content();

        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .build();

        List<String> chunks = embeddingStore.search(searchRequest)
                .matches()
                .stream()
                .map(m -> m.embedded().text())
                .collect(Collectors.toList());

        String context = String.join("\n", chunks);
        String prompt = "Use the following context to answer the question concisely.\n\nContext:\n"
                + context + "\n\nQuestion: " + question + "\nAnswer:";

        String answer = openAiChatModel.generate(prompt);
        answer = answer.replace("\\n", "\n").trim();
        List<String> answerLines = List.of(answer.split("\\r?\\n"));
        return new AnswerResponse(question, answerLines);
    }
}
