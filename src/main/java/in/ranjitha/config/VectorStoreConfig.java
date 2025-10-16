package in.ranjitha.config;

import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfig {

    @Bean
    public InMemoryEmbeddingStore<String> inMemoryEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }
}
