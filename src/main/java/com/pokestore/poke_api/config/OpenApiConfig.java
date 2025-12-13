package com.pokestore.poke_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pokeStoreOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PokeStore API")
                        .description("API RESTful para la gestión de productos y usuarios de PokeStore. " +
                                "Esta API permite realizar operaciones CRUD sobre usuarios y productos, " +
                                "así como autenticación y registro de usuarios.")
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
                ));
    }
}
