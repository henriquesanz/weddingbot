package br.com.paiva.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.github.GitHubModelsChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("github")
public class GithubModelsChatModelConfig {

    @Value("${langchain-model.github-token}")
    private String gitHubToken;

    @Value("${langchain-model.model-name}")
    private String gitHubModel;

    @Bean
    ChatLanguageModel githubModelsIChatLanguageModel() {
        return GitHubModelsChatModel.builder()
                .gitHubToken(gitHubToken)
                .modelName(gitHubModel)
                .logRequestsAndResponses(true)
                .build();
    }
}
