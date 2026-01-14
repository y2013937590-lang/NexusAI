package io.github.fyy.nexusai.service.controller;

import io.github.fyy.nexusai.common.model.Result;
import io.github.fyy.nexusai.engine.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 对话控制器
 * 
 * @author fyy
 * @since 2025-01-14
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 简单对话接口
     * @param request 请求参数
     * @return AI回复
     */
    @PostMapping("/simple")
    public Result<Map<String, Object>> simpleChat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        if (message == null || message.trim().isEmpty()) {
            return Result.fail("消息不能为空");
        }

        try {
            String response = chatService.chat(message);
            Map<String, Object> data = new HashMap<>();
            data.put("message", response);
            data.put("model", "gpt-3.5-turbo");
            return Result.success(data);
        } catch (Exception e) {
            log.error("对话失败", e);
            return Result.fail("对话失败: " + e.getMessage());
        }
    }

    /**
     * 测试LLM连接
     * @return 连接状态
     */
    @GetMapping("/test")
    public Result<Map<String, Object>> testConnection() {
        try {
            boolean connected = chatService.testConnection();
            Map<String, Object> data = new HashMap<>();
            data.put("connected", connected);
            data.put("model", "gpt-3.5-turbo");
            data.put("message", connected ? "LLM连接成功" : "LLM连接失败");
            return Result.success(data);
        } catch (Exception e) {
            log.error("测试LLM连接失败", e);
            return Result.fail("测试失败: " + e.getMessage());
        }
    }
}
