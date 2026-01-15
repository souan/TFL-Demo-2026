package lu.cnfpcfullstackdev.tfl_api.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
    name = "bearerAuth",
    description = "JWT Authentication - Use the /api/auth/login endpoint to get a token",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("TFL (Too Food To Leave) API")
                .version("1.0.0")
                .description("""
                    ## Food Rescue Platform API

                    This API enables businesses to list surplus food items and helps reduce food waste.

                    ### Authentication
                    Most endpoints require JWT authentication. To get started:
                    1. Register a new account using `/api/auth/register`
                    2. Login using `/api/auth/login` to receive a JWT token
                    3. Click the 'Authorize' button and enter your token

                    ### User Roles
                    - **BUSINESS**: Can create, update, and delete their own food listings
                    - **CONSUMER**: Can view and browse available food listings
                    - **ADMIN**: Full access to user management and all operations
                    """)
                .contact(new Contact()
                    .name("TFL API Support")
                    .email("support@tfl.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            ))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token obtained from /api/auth/login")));
    }
}
