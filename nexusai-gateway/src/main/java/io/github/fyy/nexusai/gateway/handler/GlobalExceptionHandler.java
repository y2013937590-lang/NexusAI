package io.github.fyy.nexusai.gateway.handler;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 * @author fyy
 * @since 2025-01-14
 */
@Slf4j
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 构建响应体
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("timestamp", System.currentTimeMillis());
        result.put("path", exchange.getRequest().getPath().value());

        // 根据异常类型设置状态码和消息
        if (ex instanceof ResponseStatusException) {
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            response.setStatusCode(responseStatusException.getStatusCode());
            result.put("code", responseStatusException.getStatusCode().value());
            result.put("message", responseStatusException.getReason());
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.put("message", "Internal Server Error");
        }

        log.error("Gateway Exception: ", ex);

        // 写入响应
        byte[] bytes = JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
