package com.pokestore.poke_api.controller;

import com.pokestore.poke_api.dto.CreateUserDTO;
import com.pokestore.poke_api.dto.UserDTO;
import com.pokestore.poke_api.service.SupabaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Operaciones CRUD para gestión de usuarios")
public class UserController {

    private final SupabaseService supabaseService;

    public UserController(SupabaseService supabaseService) {
        this.supabaseService = supabaseService;
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Retorna una lista con todos los usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron usuarios"
            )
    })
    @GetMapping
    public Mono<ResponseEntity<List<UserDTO>>> getAllUsers() {
        return supabaseService.selectAll("users", new ParameterizedTypeReference<List<UserDTO>>() {})
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<List<UserDTO>>notFound().build()));
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Retorna la información de un usuario específico por su ID único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> getUserById(
            @Parameter(
                    description = "ID único del usuario (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable String id) {
        return supabaseService.selectById("users", id, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Obtener usuario por email",
            description = "Busca y retorna un usuario por su dirección de email"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<UserDTO>> getUserByEmail(
            @Parameter(
                    description = "Email del usuario a buscar",
                    required = true,
                    example = "usuario@ejemplo.com"
            )
            @PathVariable String email) {
        String filter = "email=eq." + email;
        return supabaseService.selectWithFilter("users", filter, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Crear nuevo usuario",
            description = "Crea un nuevo usuario en el sistema. " +
                    "Nota: Para registro con validaciones completas, usar /api/auth/register"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error al crear usuario"
            )
    })
    @PostMapping
    public Mono<ResponseEntity<UserDTO>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserDTO.class))
            )
            @RequestBody CreateUserDTO createUserDTO) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", createUserDTO.getUsername());
        userData.put("hash_pass", createUserDTO.getPassword());
        userData.put("email", createUserDTO.getEmail());
        userData.put("rut", createUserDTO.getRut());
        
        return supabaseService.insert("users", userData, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>badRequest().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza parcialmente los datos de un usuario existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<UserDTO>> updateUser(
            @Parameter(
                    description = "ID del usuario a actualizar",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos a actualizar (solo incluir los campos que se desean modificar)",
                    required = true
            )
            @RequestBody Map<String, Object> updates) {
        return supabaseService.update("users", id, updates, new ParameterizedTypeReference<List<UserDTO>>() {})
                .flatMap(list -> {
                    if (list == null || list.isEmpty()) {
                        return Mono.just(ResponseEntity.<UserDTO>notFound().build());
                    }
                    return Mono.just(ResponseEntity.ok(list.get(0)));
                });
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina permanentemente un usuario del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(
            @Parameter(
                    description = "ID del usuario a eliminar",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable String id) {
        return supabaseService.delete("users", id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .switchIfEmpty(Mono.just(ResponseEntity.<Void>notFound().build()));
    }
}
