package io.github.fyy.nexusai.engine.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 聊天服务
 * 
 * @author fyy
 * @since 2025-01-14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatLanguageModel chatLanguageModel;

    /**
     * 简单对话
     * @param message 用户消息
     * @return AI回复
     */
    public String chat(String message) {
        try {
            log.info("发送消息到LLM: {}", message);
            String response = chatLanguageModel.generate(message);
            log.info("收到LLM回复: {}", response);
            return response;
        } catch (Exception e) {
            log.error("调用LLM失败", e);
            throw new RuntimeException("调用LLM失败: " + e.getMessage());
        }
    }

    /**
     * 测试连接
     * @return 是否连接成功
     */
    public boolean testConnection() {
        try {
            String response = chatLanguageModel.generate("Hello, this is a connection test.");
            return response != null && !response.isEmpty();
        } catch (Exception e) {
            log.error("LLM连接测试失败", e);
            return false;
        }
    }
}
