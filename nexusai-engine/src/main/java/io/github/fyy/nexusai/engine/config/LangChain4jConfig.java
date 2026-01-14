package io.github.fyy.nexusai.engine.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * LangChain4j配置类
 * 
 * @author fyy
 * @since 2025-01-14
 */
@Configuration
public class LangChain4jConfig {

    @Value("${langchain4j.openai.api-key:}")
    private String openaiApiKey;

    @Value("${langchain4j.openai.model-name:gpt-3.5-turbo}")
    private String modelName;

    @Value("${langchain4j.openai.timeout:60}")
    private Integer timeout;

    @Value("${langchain4j.openai.max-tokens:2048}")
    private Integer maxTokens;

    @Value("${langchain4j.openai.temperature:0.7}")
    private Double temperature;

    /**
     * OpenAI Chat Model Bean
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(openaiApiKey)
                .modelName(modelName)
                .timeout(Duration.ofSeconds(timeout))
                .maxTokens(maxTokens)
                .temperature(temperature)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
