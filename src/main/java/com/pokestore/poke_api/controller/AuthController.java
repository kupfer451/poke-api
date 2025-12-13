package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.AuthResponseDTO;
import com.pokestore.poke_api.dto.CreateUserDTO;
import com.pokestore.poke_api.dto.LoginDTO;
import com.pokestore.poke_api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register - Registra un nuevo usuario
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponseDTO>> register(@RequestBody CreateUserDTO createUserDTO) {
        return authService.register(createUserDTO)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.badRequest().body(response);
                });
    }

    /**
     * POST /api/auth/login - Inicia sesión
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponseDTO>> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.status(401).body(response);
                });
    }

    /**
     * GET /api/auth/verify/{email} - Verifica si un usuario existe
     */
    @GetMapping("/verify/{email}")
    public Mono<ResponseEntity<AuthResponseDTO>> verifyUser(@PathVariable String email) {
        return authService.verifyUser(email)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.status(404).body(response);
                });
    }

    /**
     * GET /api/auth/check-email/{email} - Verifica si un email está disponible
     */
    @GetMapping("/check-email/{email}")
    public Mono<ResponseEntity<Map<String, Object>>> checkEmail(@PathVariable String email) {
        return authService.checkEmailExists(email)
                .map(exists -> ResponseEntity.ok(Map.of(
                        "email", email,
                        "exists", exists,
                        "available", !exists
                )));
    }

    /**
     * GET /api/auth/check-rut/{rut} - Verifica si un RUT está disponible
     */
    @GetMapping("/check-rut/{rut}")
    public Mono<ResponseEntity<Map<String, Object>>> checkRut(@PathVariable String rut) {
        return authService.checkRutExists(rut)
                .map(exists -> ResponseEntity.ok(Map.of(
                        "rut", rut,
                        "exists", exists,
                        "available", !exists
                )));
    }
}
