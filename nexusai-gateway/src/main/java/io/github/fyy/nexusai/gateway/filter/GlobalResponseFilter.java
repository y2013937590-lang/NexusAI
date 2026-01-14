package io.github.fyy.nexusai.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局响应过滤器 - 添加响应时间
 * 
 * @author fyy
 * @since 2025-01-14
 */
@Slf4j
@Component
public class GlobalResponseFilter implements GlobalFilter, Ordered {

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录请求开始时间
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().pathWithinApplication().value();
        String method = request.getMethodValue();
        
        log.info("请求开始 - Method: {}, Path: {}", method, path);

        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                Long startTime = exchange.getAttribute(START_TIME);
                if (startTime != null) {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("请求结束 - Method: {}, Path: {}, Duration: {}ms", 
                            method, path, duration);
                    // 添加响应头
                    exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                }
            })
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
