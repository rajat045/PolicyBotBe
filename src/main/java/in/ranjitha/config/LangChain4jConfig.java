package in.ranjitha.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {

    @Value("${openai.apiKey}")
    private String openAiApiKey;


    @Bean
    public OpenAiChatModel openAiChatModel() throws Exception {
        try{
            return OpenAiChatModel.builder()
                    .apiKey(openAiApiKey)
                    .modelName("gpt-4o-mini")
                    .temperature(0.2)
                    .build();
        }catch (Exception ex){
            throw new Exception("Issue with the open api key"+ex.getMessage());
        }
    }

    @Bean
    public EmbeddingModel embeddingModel() throws Exception {
        try{
            return OpenAiEmbeddingModel.builder()
                    .apiKey(openAiApiKey)
                    .modelName("text-embedding-3-small")
                    .build();
        }catch (Exception ex){
            throw new Exception("Issue with the open api key"+ex.getMessage());
        }
    }
}
