package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * Spring Boot entrypoint for UPSTDC backend API.
 * Provides REST endpoints with JWT security, JPA persistence (PostgreSQL),
 * file upload handling, validation, global exception advice, and OpenAPI docs.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
