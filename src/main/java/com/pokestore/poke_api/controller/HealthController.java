package com.pokestore.poke_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "Endpoint para verificar el estado de la API")
public class HealthController {

    @Operation(
            summary = "Verificar estado de la API",
            description = "Retorna 'OK' si la API está funcionando correctamente. " +
                    "Útil para monitoreo y health checks de contenedores."
    )
    @ApiResponse(
            responseCode = "200",
            description = "API funcionando correctamente"
    )
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}