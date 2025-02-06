package br.com.paiva.bot.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class OllamaChatModelConfig {

    @Value("${langchain-model.ollama-url}")
    private String ollamaBaseUrl;

    @Value("${langchain-model.model-name}")
    private String modelName;

    @Bean
    ChatLanguageModel ollamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(modelName)
                .temperature(0.2)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

}
