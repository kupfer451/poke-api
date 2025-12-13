package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.AuthResponseDTO;
import com.pokestore.poke_api.dto.CreateUserDTO;
import com.pokestore.poke_api.dto.LoginDTO;
import com.pokestore.poke_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro, login y verificación de usuarios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una nueva cuenta de usuario con email, contraseña, RUT y nombre de usuario. " +
                    "El email y RUT deben ser únicos en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en el registro - Email o RUT ya existen",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            )
    })
    @PostMapping("/register")
    public Mono<ResponseEntity<AuthResponseDTO>> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario a registrar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserDTO.class))
            )
            @RequestBody CreateUserDTO createUserDTO) {
        return authService.register(createUserDTO)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.badRequest().body(response);
                });
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario existente con su email y contraseña. " +
                    "Retorna la información del usuario si las credenciales son válidas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas - Usuario no encontrado o contraseña incorrecta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            )
    })
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponseDTO>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales de inicio de sesión",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
            )
            @RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.status(401).body(response);
                });
    }

    @Operation(
            summary = "Verificar usuario por email",
            description = "Verifica si un usuario existe en el sistema y retorna su información pública"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            )
    })
    @GetMapping("/verify/{email}")
    public Mono<ResponseEntity<AuthResponseDTO>> verifyUser(
            @Parameter(
                    description = "Email del usuario a verificar",
                    required = true,
                    example = "usuario@ejemplo.com"
            )
            @PathVariable String email) {
        return authService.verifyUser(email)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    }
                    return ResponseEntity.status(404).body(response);
                });
    }

    @Operation(
            summary = "Verificar disponibilidad de email",
            description = "Comprueba si un email ya está registrado en el sistema. " +
                    "Útil para validación en formularios de registro."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Resultado de la verificación de disponibilidad"
    )
    @GetMapping("/check-email/{email}")
    public Mono<ResponseEntity<Map<String, Object>>> checkEmail(
            @Parameter(
                    description = "Email a verificar",
                    required = true,
                    example = "nuevo@ejemplo.com"
            )
            @PathVariable String email) {
        return authService.checkEmailExists(email)
                .map(exists -> ResponseEntity.ok(Map.of(
                        "email", email,
                        "exists", exists,
                        "available", !exists
                )));
    }

    @Operation(
            summary = "Verificar disponibilidad de RUT",
            description = "Comprueba si un RUT chileno ya está registrado en el sistema. " +
                    "Útil para validación en formularios de registro."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Resultado de la verificación de disponibilidad"
    )
    @GetMapping("/check-rut/{rut}")
    public Mono<ResponseEntity<Map<String, Object>>> checkRut(
            @Parameter(
                    description = "RUT chileno a verificar (con guión y dígito verificador)",
                    required = true,
                    example = "12345678-9"
            )
            @PathVariable String rut) {
        return authService.checkRutExists(rut)
                .map(exists -> ResponseEntity.ok(Map.of(
                        "rut", rut,
                        "exists", exists,
                        "available", !exists
                )));
    }
}
