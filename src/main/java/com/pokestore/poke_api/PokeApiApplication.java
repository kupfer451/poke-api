package com.pokestore.poke_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PokeApiApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(PokeApiApplication.class, args);
        Environment env = context.getEnvironment();
        
        // Verificar configuración
        System.out.println("=== CONFIGURACIÓN CARGADA ===");
        System.out.println("DB URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("DB Username: " + env.getProperty("spring.datasource.username"));
        System.out.println("Supabase URL: " + env.getProperty("supabase.url"));
        System.out.println("============================");
    }

}
