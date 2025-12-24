package br.com.robertasantos.balanceapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Balance API",
                version = "1.0.0",
                description = "In-memory Balance API for take-home test (GET /balance, POST /event, POST /reset)."
        )
)
public class OpenApiConfig { }
