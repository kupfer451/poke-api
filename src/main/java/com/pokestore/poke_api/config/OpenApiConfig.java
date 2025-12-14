package com.pokestore.poke_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pokeStoreOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("PokeStore API")
                        .description("API RESTful para la gestión de productos, usuarios y órdenes de PokeStore. " +
                                "Esta API permite realizar operaciones CRUD sobre usuarios, productos y órdenes, " +
                                "así como autenticación y registro de usuarios.\n\n" +
                                "## Autenticación\n" +
                                "Los endpoints protegidos requieren un token JWT en el header `Authorization: Bearer <token>`.\n\n" +
                                "## Roles\n" +
                                "- **Usuario**: Puede ver productos, crear órdenes y ver sus propias órdenes.\n" +
                                "- **Admin**: Puede gestionar productos, ver todas las órdenes y cambiar estados.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PokeStore Team")
                                .email("contacto@pokestore.com")
                                .url("https://pokestore.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("https://api.pokestore.com")
                                .description("Servidor de producción")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingresa el token JWT obtenido del login o registro")));
    }
}
