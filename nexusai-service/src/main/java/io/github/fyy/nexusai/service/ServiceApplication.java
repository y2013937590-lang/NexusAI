package io.github.fyy.nexusai.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NexusAI Service 启动类
 * 
 * @author fyy
 * @since 2025-01-14
 */
@SpringBootApplication(scanBasePackages = "io.github.fyy.nexusai")
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
        System.out.println("""
                
                ╔═══════════════════════════════════════════════════════════════╗
                ║                                                               ║
                ║   NexusAI Service Started Successfully!                       ║
                ║                                                               ║
                ║   Service Port: 8081                                          ║
                ║   Actuator:     /actuator                                     ║
                ║   Metrics:      /actuator/prometheus                          ║
                ║                                                               ║
                ╚═══════════════════════════════════════════════════════════════╝
                """);
    }
}
