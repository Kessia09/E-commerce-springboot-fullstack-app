package org.cat.irere.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Configuration class for OpenAPI documentation.
 */
@Configuration
public class OpenApiConfig {

        private static final String SECURITY_SCHEME_NAME = "bearerAuth";

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                                .components(new Components()
                                                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                                                new SecurityScheme()
                                                                                .name(SECURITY_SCHEME_NAME)
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description(
                                                                                                "Provide the JWT token. JWT token can be obtained from the Login API.")))
                                .info(new Info()
                                                .title("Binary Supermarket API")
                                                .description(
                                                                "REST API for Binary Supermarket online shopping system. This API provides endpoints for managing products, user authentication, shopping cart, and purchases.")
                                                .version("1.0")
                                                .contact(new Contact()
                                                                .name("Binary Supermarket Admin")
                                                                .email("irere205@gmail.com")
                                                                .url("https://github.com/Irere123/e-commerce-spring-boot"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .tags(getTags());
        }

        @Bean
        public GroupedOpenApi publicApi() {
                return GroupedOpenApi.builder()
                                .group("public-api")
                                .packagesToScan("org.cat.irere.controllers")
                                .build();
        }

        private List<Tag> getTags() {
                return Arrays.asList(
                                new Tag().name("Authentication")
                                                .description("Operations related to user authentication"),
                                new Tag().name("Products").description("Operations related to product management"),
                                new Tag().name("Cart").description("Operations related to shopping cart management"),
                                new Tag().name("Quantity")
                                                .description("Operations related to product quantity management"),
                                new Tag().name("Purchase").description("Operations related to purchase management"),
                                new Tag().name("Customer").description("Operations related to customer profiles"));
        }
}