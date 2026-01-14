package com.easyflow.creditapproval.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI easyFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EasyFlow - Credit Approval API")
                        .description("API para orquestração de propostas de crédito com Spring Boot + Camunda BPM 7.")
                        .version("1.0.0")
                        .contact(new Contact().name("EasyFlow"))
                        .license(new License().name("MIT")));
    }
}
