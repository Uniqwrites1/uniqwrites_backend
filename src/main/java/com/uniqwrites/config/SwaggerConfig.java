package com.uniqwrites.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI uniqwritesOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Uniqwrites API")
                .description("API documentation for Uniqwrites Backend")
                .version("v1.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/public/**", "/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tutorApi() {
        return GroupedOpenApi.builder()
                .group("tutor")
                .pathsToMatch("/api/tutor/**")
                .build();
    }

    @Bean
    public GroupedOpenApi studentApi() {
        return GroupedOpenApi.builder()
                .group("student")
                .pathsToMatch("/api/student/**")
                .build();
    }

    @Bean
    public GroupedOpenApi requestFormApi() {
        return GroupedOpenApi.builder()
                .group("request-form")
                .pathsToMatch("/request-form/**")
                .build();
    }
}
