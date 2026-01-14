package io.github.fyy.nexusai.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NexusAI API Gateway 启动类
 * 
 * @author fyy
 * @since 2025-01-14
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("""
                
                ╔═══════════════════════════════════════════════════════════════╗
                ║                                                               ║
                ║   NexusAI Gateway Started Successfully!                       ║
                ║                                                               ║
                ║   Gateway Port: 8080                                          ║
                ║   Actuator:     /actuator                                     ║
                ║   Metrics:      /actuator/prometheus                          ║
                ║                                                               ║
                ╚═══════════════════════════════════════════════════════════════╝
                """);
    }
}
