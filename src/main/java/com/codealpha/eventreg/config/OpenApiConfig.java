package com.codealpha.eventreg.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
                .title("Event Registration System API")
                .version("1.0.0"));
    }
}
