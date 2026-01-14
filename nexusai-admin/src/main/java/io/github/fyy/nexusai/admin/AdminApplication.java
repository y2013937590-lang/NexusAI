package io.github.fyy.nexusai.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NexusAI Admin 启动类
 * 
 * @author fyy
 * @since 2025-01-14
 */
@SpringBootApplication(scanBasePackages = "io.github.fyy.nexusai")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
        System.out.println("""
                
                ╔═══════════════════════════════════════════════════════════════╗
                ║                                                               ║
                ║   NexusAI Admin Started Successfully!                         ║
                ║                                                               ║
                ║   Admin Port:   8082                                          ║
                ║   Actuator:     /actuator                                     ║
                ║   Metrics:      /actuator/prometheus                          ║
                ║                                                               ║
                ╚═══════════════════════════════════════════════════════════════╝
                """);
    }
}
