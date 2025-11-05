package org.example.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PUBLIC_INTERFACE
 * OpenAPI configuration for Swagger UI and API docs.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI upstdcOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("UPSTDC Infrastructure API")
                .version("1.0.0")
                .description("REST API backend providing business logic and secure data access for UPSTDC modules.")
                .contact(new Contact().name("UPSTDC").email("support@upstdc.example")))
            .externalDocs(new ExternalDocumentation()
                .description("API Docs")
                .url("/swagger-ui.html"));
    }
}
