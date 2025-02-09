package br.com.paiva.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("openai")
public class OpenAiChatModelConfig {

    @Value("${langchain-model.openai-api-key}")
    private String openAiApiKey;

    @Value("${langchain-model.model-name}")
    private String openAiModel;

    @Bean
    ChatLanguageModel openAiChatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(openAiApiKey)
                .modelName(openAiModel)
                .logResponses(true)
                .logRequests(true)
                .build();
    }
}
